import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import AddItem from './pages/AddItem';
import { useState, useEffect } from 'react';
import axios from 'axios';

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isLoading, setIsLoading] = useState(true);

    const checkAuth = async () => {
        const token = localStorage.getItem('authToken');
        if (token) {
            console.log("Avem token " + token);
            try {
                const response = await axios.get('/auth/validate-token', {
                    headers: { Authorization: `${token}` }
                });
                if (response.status === 200) {
                    setIsAuthenticated(true);
                }
            } catch (error) {
                console.error('Token validation failed', error);
                localStorage.removeItem('authToken');
                setIsAuthenticated(false);
            }
        } else {
            setIsAuthenticated(false);
        }
        setIsLoading(false);
    };

    useEffect(() => {
        checkAuth();
        window.addEventListener('storage', checkAuth);
        return () => window.removeEventListener('storage', checkAuth);
    }, []);

    if (isLoading) {
        return <div className="loading">Loading...</div>;
    }

    return (
        <Router>
            <Routes>
                <Route path="/login" element={
                    !isAuthenticated ? <Login setIsAuthenticated={setIsAuthenticated} /> : <Navigate to="/dashboard" />
                } />
                <Route path="/register" element={
                    !isAuthenticated ? <Register /> : <Navigate to="/dashboard" />
                } />
                <Route path="/dashboard" element={
                    isAuthenticated ? <Dashboard /> : <Navigate to="/login" />
                } />
                <Route path="/add-item" element={
                    isAuthenticated ? <AddItem /> : <Navigate to="/login" />
                } />
                <Route path="/" element={<Navigate to={isAuthenticated ? "/dashboard" : "/login"} />} />
            </Routes>
        </Router>
    );
}

export default App;