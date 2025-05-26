import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/Login.css';

const Login = ({ setIsAuthenticated }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        try {
            const response = await axios.post('/auth/login', {
                email,
                password
            });

            localStorage.setItem('authToken', response.data.token);
            localStorage.setItem('user', JSON.stringify(response.data.user));

            setIsAuthenticated(true);

            navigate('/dashboard');
        } catch (err) {
            if (err.response && err.response.data) {
                setError(err.response.data.error);
            } else {
                setError('An error occurred during login. Please try again.');
            }
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="login-container">
            <div className="login-card">
                <h2>Welcome to BorrowIt</h2>
                <p className="login-subtitle">Sign in to continue</p>

                {error && <div className="error-message">{error}</div>}

                <form onSubmit={handleSubmit} className="login-form">
                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <input
                            type="email"
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="Enter your email"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="Enter your password"
                            required
                        />
                    </div>

                    <button
                        type="submit"
                        className="login-button"
                        disabled={isLoading}
                    >
                        {isLoading ? 'Signing in...' : 'Sign In'}
                    </button>
                </form>

                <div className="login-footer">
                    <p>Don't have an account? <a href="/register">Sign Up</a></p>
                    <a href="/forgot-password" className="forgot-password">Forgot Password?</a>
                </div>
            </div>
        </div>
    );
};

export default Login;