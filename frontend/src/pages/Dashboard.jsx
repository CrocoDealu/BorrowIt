// src/components/Dashboard.jsx
import { useState, useEffect } from 'react';
import axios from 'axios';
import AppLayout from './AppLayout';
import '../styles/Dashboard.css';
import ItemCard from "./ItemCard.jsx";

const Dashboard = () => {
    const [items, setItems] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchItems = async () => {
            try {
                const response = await axios.get('/items', {
                    headers: {
                        Authorization: `${localStorage.getItem('authToken')}`
                    }
                });
                console.log('API Response:', response.data);
                setItems(response.data);
            } catch (error) {
                console.error('Error fetching items:', error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchItems();
    }, []);

    if (isLoading) {
        return <div className="loading">Loading...</div>;
    }

    return (
        <AppLayout>
            <div className="dashboard-section">
                <h2>Items</h2>
                {items.length > 0 ? (
                    <div className="items-grid">
                        {items.map((item) => (
                            <ItemCard key={item.id} item={item} type="viewer" onDelete={null}/>
                        ))}
                    </div>
                ) : (
                    <p className="no-items">You haven't added any items yet.</p>
                )}
            </div>
        </AppLayout>
    );
};

export default Dashboard;