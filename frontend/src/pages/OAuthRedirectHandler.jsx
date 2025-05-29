import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

function OAuthRedirectHandler() {
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        const queryParams = new URLSearchParams(location.search);
        const token = queryParams.get('token');

        if (token) {
            localStorage.setItem('authToken', token);

            fetch('http://localhost:8080/api/auth/me', {
                headers: {
                    'Authorization': `${token}`
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to fetch user data');
                    }
                    console.log("User data fetched successfully");
                    return response.json();
                })
                .then(userData => {
                    localStorage.setItem('user', JSON.stringify(userData));
                    setLoading(false);
                    window.dispatchEvent(new Event('storage'));
                    navigate('/dashboard');
                })
                .catch(err => {
                    console.error('Error fetching user data:', err);
                    setError('Authentication failed. Please try again.');
                    setLoading(false);
                });
        } else {
            setError('No authentication token received');
            setLoading(false);
        }
    }, [location, navigate]);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div className="error-message">{error}</div>;
    }

    return null;
}

export default OAuthRedirectHandler;