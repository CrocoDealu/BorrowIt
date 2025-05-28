import { useState, useEffect } from 'react';
import axios from 'axios';
import './ItemCard.jsx';
import AppLayout from './AppLayout';
import ItemCard from "./ItemCard.jsx";

const BorrowedItems = () => {
    const [borrowedItems, setBorrowedItems] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchBorrowedItems = async () => {
            try {
                const token = localStorage.getItem('authToken');
                const response = await axios.get('/rentals/user', {
                    headers: {
                        Authorization: `${token}`
                    }
                });
                setBorrowedItems(response.data);
            } catch (error) {
                console.error('Error fetching borrowed items:', error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchBorrowedItems();
    }, []);

    const handleReturnItem = async (itemId) => {
        try {
            await axios.post(`/items/${itemId}/return`, null, {
                headers: {
                    Authorization: `${localStorage.getItem('authToken')}`
                }
            });
            setBorrowedItems(borrowedItems.filter(item => item.id !== itemId));
        } catch (error) {
            console.error('Error returning item:', error);
        }
    };

    if (isLoading) {
        return <div className="loading">Loading...</div>;
    }

    return (
        <AppLayout>
            <div className="dashboard-section">
                <h2>Borrowed Items</h2>
                {borrowedItems.length > 0 ? (
                    <div className="items-grid">
                        {borrowedItems.map((item) => (
                            <ItemCard
                                key={item.id}
                                item={item}
                                type="borrowed"
                                onReturn={handleReturnItem}
                            />
                        ))}
                    </div>
                ) : (
                    <p className="no-items">You haven't borrowed any items yet.</p>
                )}
            </div>
        </AppLayout>
    );
};

export default BorrowedItems;