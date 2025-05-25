import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/Dashboard.css';

const Dashboard = () => {
    const [user, setUser] = useState(null);
    const [items, setItems] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const authToken = localStorage.getItem('authToken');
        if (!authToken) {
            navigate('/login');
            return;
        }

        const userData = localStorage.getItem('user');
        if (userData) {
            setUser(JSON.parse(userData));
        }

        const fetchItems = async () => {
            try {
                const response = await axios.get('/items');
                console.log('API Response:', response.data);
                setItems(response.data);
            } catch (error) {
                console.error('Error fetching items:', error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchItems();
    }, [navigate]);

    const handleLogout = async () => {
        try {
            const token = localStorage.getItem('authToken');
            localStorage.removeItem('authToken');
            localStorage.removeItem('user');
            const response = await axios.post('/auth/logout', null, {
                headers: {
                    'Authorization': `${token}`
                }
            });

            console.log('Logout successful:', response.data);

            localStorage.removeItem('authToken');

            window.dispatchEvent(new Event('storage'));
            navigate('/login');
        } catch (error) {
            console.error('Logout failed:', error);
        } finally {
            setIsLoading(false);
        }
    };

    if (isLoading) {
        return <div className="loading">Loading...</div>;
    }

    return (
        <div className="dashboard-container">
            <header className="dashboard-header">
                <h1>BorrowIt Dashboard</h1>
                <div className="user-info">
                    {user && (
                        <>
                            <span>Welcome, {user.firstName || user.name}</span>
                            <button onClick={handleLogout} className="logout-button">Logout</button>
                        </>
                    )}
                </div>
            </header>

            <div className="dashboard-content">
                <div className="dashboard-section">
                    <h2>My Items</h2>
                    {items.length > 0 ? (
                        <div className="items-grid">
                            {items.map((item) => {
                                const imageUrl = item.images && item.images.length > 0
                                    ? URL.createObjectURL(new Blob([item.images[0]]))
                                    : null;

                                return (
                                    <div key={item.id} className="item-card">
                                        {imageUrl && (
                                            <img src={imageUrl} alt={item.name} className="item-image"/>
                                        )}
                                        <div className="item-details">
                                            <h3>{item.title}</h3>
                                            <p>{item.description}</p>
                                            <div className="item-status">
                                                Status: <span>{item.status || 'Available'}</span>
                                            </div>
                                        </div>
                                    </div>
                                );
                            })}
                        </div>
                    ) : (
                        <p className="no-items">You haven't added any items yet.</p>
                    )}
                </div>
                <div className="dashboard-actions">
                    <button
                        className="action-button add-item"
                        onClick={() => navigate('/add-item')}
                    >Add New Item
                    </button>
                    <button className="action-button view-borrowed">View Borrowed Items</button>
                    <button className="action-button view-lent">View Lent Items</button>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;