import React, { useState } from 'react';
import PropTypes from 'prop-types';

const statuses = ['APPROVED', 'REJECTED', 'RETURNED', 'OVERDUE', 'CANCELLED', 'PENDING'];

const RentItem = ({ initialStartDate, initialEndDate, initialStatus, onSubmit, onCancel }) => {
    const [startDate, setStartDate] = useState(initialStartDate || '');
    const [endDate, setEndDate] = useState(initialEndDate || '');

    const handleSubmit = (e) => {
        e.preventDefault();

        if (new Date(startDate) > new Date(endDate)) {
            alert('Start date cannot be after end date');
            return;
        }

        if (new Date(startDate) < Date.now()) {
            alert('Start date cannot be in the past');
            return;
        }

        const rentDetails = {
            startDate,
            endDate,
            initialStatus,
        };

        onSubmit(rentDetails);
    };

    return (
        <div className="rent-item-container">
            <form onSubmit={handleSubmit} className="rent-item-form">
                <div>
                    <label htmlFor="startDate">Start Date:</label>
                    <input
                        type="date"
                        id="startDate"
                        value={startDate}
                        onChange={(e) => setStartDate(e.target.value)}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="endDate">End Date:</label>
                    <input
                        type="date"
                        id="endDate"
                        value={endDate}
                        onChange={(e) => setEndDate(e.target.value)}
                        required
                    />
                </div>

                <button type="submit">Submit</button>
            </form>
            <button onClick={onCancel} className="action-button cancel-button">
                Cancel
            </button>
        </div>
    );
};

RentItem.propTypes = {
    initialStartDate: PropTypes.string,
    initialEndDate: PropTypes.string,
    initialStatus: PropTypes.oneOf(statuses),
    onSubmit: PropTypes.func.isRequired,
    onCancel: PropTypes.func.isRequired, // Cancel callback is now required
};

export default RentItem;