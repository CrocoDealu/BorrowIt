import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import AppLayout from './AppLayout';
import '../styles/ViewItem.css';
import RentItem from "./RentItem.jsx";

const ViewItem = () => {
    const { id } = useParams();
    const [item, setItem] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();
    const [user, setUser] = useState(null);
    const [isOwner, setIsOwner] = useState(false);
    const [showRentItemForm, setShowRentItemForm] = useState(false);
    const [rentDetails, setRentDetails] = useState(null);


    useEffect(() => {
        const fetchData = async () => {
            try {
                const userData = JSON.parse(localStorage.getItem('user'));
                setUser(userData);

                const response = await axios.get(`/items/${id}`, {
                    headers: {
                        Authorization: `${localStorage.getItem('authToken')}`
                    }
                });

                setItem(response.data);

                if (isUserOwner(userData, response.data)) {
                    setIsOwner(true);
                } else {
                    setIsOwner(false);
                }

            } catch (error) {
                console.error('Error fetching item details:', error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchData();
    }, [id]);

    const isUserOwner = (user, item) => {
        return item.ownerHash === user.userHash;
    };

    const renderValue = (value, defaultValue = 'Unknown') => {
        if (value === null || value === undefined) {
            return defaultValue;
        }
        if (typeof value === 'object') {
            if (value.name) return value.name;
            if (value.toString && value.toString !== Object.prototype.toString) {
                return value.toString();
            }
            return JSON.stringify(value);
        }
        return value;
    };

    const handleBorrowRequest = () => {
        setShowRentItemForm(true);
    };

    const handleRentItemSubmit = async (details) => {
        setRentDetails(details);
        setShowRentItemForm(false);
        console.log('Rent details submitted:', details);

        try {
            let response = await axios.post(`rentals/rent/${id}`, details, {
                headers: {
                    Authorization: `${localStorage.getItem('authToken')}`,
                    'Content-Type': 'application/json',
                },
            });

            console.log('API Response:', response.data);
        } catch (error) {
            console.error('Error submitting rent details:', error);
        }
    };

    const handleCancel = () => {
        setShowRentItemForm(false);
    };

    if (isLoading) {
        return (
            <AppLayout>
                <div className="loading">Loading item details...</div>
            </AppLayout>
        );
    }

    if (!item) {
        return (
            <AppLayout>
                <div className="error-container">Item not found</div>
            </AppLayout>
        );
    }

    return (
        <AppLayout>
            <div className="dashboard-section item-detail-section">
                <h2>Item Details</h2>
                <div className="item-detail-container">
                    <div className="item-detail-image">
                        <img
                            src={item.images && item.images.length > 0
                                ? "data:image/png;base64," + item.images[0]
                                : "/no_image.jpeg"}
                            alt={renderValue(item.title)}
                        />
                        {item.images && item.images.length > 1 && (
                            <div className="item-additional-images">
                                {item.images.slice(1).map((image, index) => (
                                    <img
                                        key={index}
                                        src={`data:image/png;base64, ${image}`}
                                        alt={`${renderValue(item.title)} - additional view ${index + 1}`}
                                        className="additional-image"
                                    />
                                ))}
                            </div>
                        )}
                    </div>
                    <div className="item-detail-info">
                        <h3 className="item-detail-title">{item.title}</h3>
                        <div className="item-detail-meta">
                            <div className="meta-item">
                                <span className="meta-label">Status:</span>
                                <span className="meta-value">{item.status}</span>
                            </div>
                            <div className="meta-item">
                                <span className="meta-label">Owner:</span>
                                <span className="meta-value">{item.owner}</span>
                            </div>
                            {item.category && (
                                <div className="meta-item">
                                    <span className="meta-label">Category:</span>
                                    <span className="meta-value">{item.category}</span>
                                </div>
                            )}
                        </div>

                        <div className="item-detail-description">
                            <h4>Description</h4>
                            <p>{item.description}</p>
                        </div>

                        {item.borrowingConditions && (
                            <div className="item-borrowing-conditions">
                                <h4>Borrowing Conditions</h4>
                                <p>{renderValue(item.borrowingConditions)}</p>
                            </div>
                        )}

                        <div className="item-detail-actions">
                            <button
                                onClick={() => navigate(-1)}
                                className="back-button"
                            >
                                Back
                            </button>

                            {/* Only show borrow button if the user is not the owner */}
                            {!isOwner && renderValue(item.status) !== 'UNAVAILABLE' && (
                                <button
                                    onClick={handleBorrowRequest}
                                    className="action-button borrow-button"
                                >
                                    Request to Borrow
                                </button>
                            )}

                            {/* If user is the owner, show edit button */}
                            {isOwner && (
                                <button
                                    onClick={() => navigate(`/edit-item/${id}`)}
                                    className="action-button edit-button"
                                >
                                    Edit Item
                                </button>
                            )}
                        </div>
                    </div>
                    {showRentItemForm && (
                        <div className="rent-item-modal">
                            <RentItem
                                initialStartDate=""
                                initialEndDate=""
                                status="PENDING"
                                onSubmit={handleRentItemSubmit}
                                onCancel={handleCancel}
                            />
                        </div>
                    )}
                </div>
            </div>
        </AppLayout>
    );
};

export default ViewItem;