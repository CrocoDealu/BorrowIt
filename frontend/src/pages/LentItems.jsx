// src/components/LentItems.jsx
import { useState, useEffect } from 'react';
import axios from 'axios';
import './ItemCard.jsx';
import AppLayout from './AppLayout';
import ItemCard from "./ItemCard.jsx";

const LentItems = () => {
    const [lentItems, setLentItems] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchLentItems = async () => {
            try {
                const token = localStorage.getItem('authToken');
                const response = await axios.get('/items/lent/user', {
                    headers: {
                        Authorization: `${token}`
                    }
                });
                setLentItems(response.data);
            } catch (error) {
                console.error('Error fetching lent items:', error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchLentItems();
    }, []);

    const handleDeleteItem = async (itemId) => {
        try {
            await axios.delete(`items/${itemId}`, {
                headers: {
                    Authorization: `${localStorage.getItem('authToken')}`
                }
            });
            setLentItems(lentItems.filter(item => item.id !== itemId));
        } catch (error) {
            console.error('Error deleting item:', error);
        }
    };

    if (isLoading) {
        return <div className="loading">Loading...</div>;
    }

    return (
        <AppLayout>
            <div className="dashboard-section">
                <h2>Lent Items</h2>
                {lentItems.length > 0 ? (
                    <div className="items-grid">
                        {lentItems.map((item) => (
                            <ItemCard
                                key={item.id}
                                item={item}
                                type="lent"
                                onDelete={handleDeleteItem}
                            />
                        ))}
                    </div>
                ) : (
                    <p className="no-items">You haven't lent any items yet.</p>
                )}
            </div>
        </AppLayout>
    );
};

export default LentItems;