import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/AddItem.css';

const AddItem = () => {
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        category: '',
        condition: '',
        rentPrice: '',
        location: '',
        status: 'AVAILABLE' // Default value
    });

    // Keep selected files in a separate state
    const [selectedFiles, setSelectedFiles] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');
    const [imagePreviews, setImagePreviews] = useState([]);
    const navigate = useNavigate();

    // These should match your backend enums
    const categories = [
        'ELECTRONICS', 'BOOKS', 'TOOLS', 'KITCHEN', 'SPORTS',
        'OUTDOOR', 'CLOTHING', 'FURNITURE', 'GAMES', 'OTHER'
    ];

    const conditions = [
        'NEW', 'LIKE_NEW', 'VERY_GOOD', 'GOOD', 'FAIR', 'POOR'
    ];

    const statuses = [
        'AVAILABLE', 'UNAVAILABLE', 'RENTED'
    ];

    const handleChange = (e) => {
        const { name, value } = e.target;
        if (name === 'rentPrice') {
            const numValue = value === '' ? '' : parseFloat(value);
            setFormData({
                ...formData,
                [name]: numValue
            });
        } else {
            setFormData({
                ...formData,
                [name]: value
            });
        }
    };

    const handleImageChange = (e) => {
        const files = Array.from(e.target.files);
        if (files.length === 0) return;

        // Create previews for the newly selected files
        const newPreviews = files.map(file => URL.createObjectURL(file));
        setImagePreviews([...imagePreviews, ...newPreviews]);

        // Store the actual file objects for later upload
        setSelectedFiles([...selectedFiles, ...files]);
    };

    const removeImage = (index) => {
        // Remove from previews
        const newPreviews = [...imagePreviews];
        newPreviews.splice(index, 1);
        setImagePreviews(newPreviews);

        // Remove from selected files
        const newFiles = [...selectedFiles];
        newFiles.splice(index, 1);
        setSelectedFiles(newFiles);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        try {
            const token = localStorage.getItem('authToken');
            if (!token) {
                throw new Error('You must be logged in to add an item');
            }

            if (!formData.title || !formData.category || !formData.condition || !formData.rentPrice) {
                throw new Error('Please fill in all required fields');
            }

            const formDataToSend = new FormData();

            // Add the 'item' part as JSON
            const itemPart = {
                title: formData.title,
                category: formData.category,
                condition: formData.condition,
                rentPrice: formData.rentPrice,
                description: formData.description,
                location: formData.location,
                status: formData.status,
                owner: null,
            };
            formDataToSend.append('item', new Blob([JSON.stringify(itemPart)], { type: 'application/json' }));

            // Add files with correct keys
            selectedFiles.forEach((file, index) => {
                formDataToSend.append('files', file);
            });
            console.log(formDataToSend);

            const response = await axios.post('/items', formDataToSend, {
                headers: {
                    'Authorization': `${token}`,
                    'Content-Type': 'multipart/form-data'
                }
            });

            navigate('/dashboard', {
                state: {
                    message: 'Item added successfully!',
                    itemId: response.data.id
                }
            });

        } catch (err) {
            if (err.response && err.response.data) {
                setError(err.response.data.error || 'Failed to add item. Please try again.');
            } else {
                setError(err.message || 'An error occurred. Please try again.');
            }
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="add-item-container">
            <div className="add-item-card">
                <h2>Add New Item</h2>
                <p className="add-item-subtitle">Share something with your community</p>

                {error && <div className="error-message">{error}</div>}

                <form onSubmit={handleSubmit} className="add-item-form">
                    <div className="form-group">
                        <label htmlFor="title">Item Title*</label>
                        <input
                            type="text"
                            id="title"
                            name="title"
                            value={formData.title}
                            onChange={handleChange}
                            placeholder="What are you lending?"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="description">Description</label>
                        <textarea
                            id="description"
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            placeholder="Provide details about your item"
                            rows="4"
                        />
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="category">Category*</label>
                            <select
                                id="category"
                                name="category"
                                value={formData.category}
                                onChange={handleChange}
                                required
                            >
                                <option value="" disabled>Select a category</option>
                                {categories.map(category => (
                                    <option key={category} value={category}>{category.replace('_', ' ')}</option>
                                ))}
                            </select>
                        </div>

                        <div className="form-group">
                            <label htmlFor="condition">Condition*</label>
                            <select
                                id="condition"
                                name="condition"
                                value={formData.condition}
                                onChange={handleChange}
                                required
                            >
                                <option value="" disabled>Select condition</option>
                                {conditions.map(condition => (
                                    <option key={condition} value={condition}>
                                        {condition.replace('_', ' ')}
                                    </option>
                                ))}
                            </select>
                        </div>
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="rentPrice">Rental Price per Day*</label>
                            <input
                                type="number"
                                id="rentPrice"
                                name="rentPrice"
                                value={formData.rentPrice}
                                onChange={handleChange}
                                placeholder="0.00"
                                step="0.01"
                                min="0"
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="location">Location</label>
                            <input
                                type="text"
                                id="location"
                                name="location"
                                value={formData.location}
                                onChange={handleChange}
                                placeholder="Where is this item located?"
                            />
                        </div>
                    </div>

                    <div className="form-group">
                        <label htmlFor="status">Status*</label>
                        <select
                            id="status"
                            name="status"
                            value={formData.status}
                            onChange={handleChange}
                            required
                        >
                            {statuses.map(status => (
                                <option key={status} value={status}>
                                    {status.replace('_', ' ')}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label htmlFor="images">Item Photos</label>
                        <input
                            type="file"
                            id="images"
                            name="images"
                            onChange={handleImageChange}
                            accept="image/*"
                            multiple
                            className="file-input"
                        />
                        <label htmlFor="images" className="file-label">
                            Choose Photos
                        </label>

                        {imagePreviews.length > 0 && (
                            <div className="images-preview-container">
                                {imagePreviews.map((src, index) => (
                                    <div key={index} className="image-preview-item">
                                        <img src={src} alt={`Preview ${index + 1}`} />
                                        <button
                                            type="button"
                                            onClick={() => removeImage(index)}
                                            className="remove-image-btn"
                                        >
                                            ✕
                                        </button>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>

                    <div className="form-buttons">
                        <button
                            type="button"
                            onClick={() => navigate('/dashboard')}
                            className="cancel-button"
                            disabled={isLoading}
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            className="submit-button"
                            disabled={isLoading}
                        >
                            {isLoading ? 'Adding Item...' : 'Add Item'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default AddItem;