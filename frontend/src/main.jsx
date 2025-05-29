import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import './index.css';
import axios from 'axios';
import {GoogleOAuthProvider} from "@react-oauth/google";

axios.defaults.baseURL = 'http://localhost:8080/api';
const GOOGLE_CLIENT_ID = "251918828330-a7o77l16of5f5pnh916rqiripi1m0lb2.apps.googleusercontent.com";

axios.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('authToken');
        if (token) {
            config.headers['Authorization'] = `${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <GoogleOAuthProvider clientId={GOOGLE_CLIENT_ID}>
            <App />
        </GoogleOAuthProvider>
    </React.StrictMode>
);