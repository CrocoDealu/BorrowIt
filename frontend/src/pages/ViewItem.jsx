import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import AppLayout from './AppLayout';
import '../styles/ViewItem.css';

const ViewItem = () => {
    const { id } = useParams();
    const [item, setItem] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();
    const [user, setUser] = useState(null);
    const [isOwner, setIsOwner] = useState(false);

    useEffect(() => {
        const fetchData = async () => {
            try {
                // Get the currently logged in user
                const userData = JSON.parse(localStorage.getItem('user'));
                setUser(userData);

                console.log(id);
                const response = await axios.get(`/items/${id}`, {
                    headers: {
                        Authorization: `${localStorage.getItem('authToken')}`
                    }
                });

                setItem(response.data);

                checkIfUserIsOwner(userData, response.data);

            } catch (error) {
                console.error('Error fetching item details:', error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchData();
    }, [id]);

    const checkIfUserIsOwner = (user, item) => {
        // Your ownership check logic here
        return true; // For now, always return true as in your example
    };

    const handleBorrowRequest = async () => {
        try {
            await axios.post(`/api/items/${id}/borrow`, {}, {
                headers: {
                    Authorization: `${localStorage.getItem('authToken')}`
                }
            });
            alert('Borrow request sent successfully!');
            navigate('/dashboard');
        } catch (error) {
            console.error('Error sending borrow request:', error);
            alert('Failed to send borrow request. Please try again.');
        }
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
                            {!isOwner && renderValue(item.status) === 'Available' && (
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
                </div>
            </div>
        </AppLayout>
    );
};

export default ViewItem;