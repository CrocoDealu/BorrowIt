// src/components/AppLayout.jsx
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const AppLayout = ({ children, pageTitle }) => {
    const [user, setUser] = useState(null);
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

        setIsLoading(false);
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
        }
    };

    if (isLoading) {
        return <div className="loading">Loading...</div>;
    }

    return (
        <div className="dashboard-container">
            <header className="dashboard-header">
                <h1>BorrowIt Dashboard</h1>
                <div className="header-actions">
                    <button
                        className="action-button add-item"
                        onClick={() => navigate('/add-item')}
                    >Add New Item
                    </button>
                    <button
                        className="action-button view-borrowed"
                        onClick={() => navigate('/borrowed-items')}
                    >View Borrowed Items</button>
                    <button
                        className="action-button view-lent"
                        onClick={() => navigate('/lent-items')}
                    >View Lent Items</button>
                </div>
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
                {children}
            </div>
        </div>
    );
};

export default AppLayout;