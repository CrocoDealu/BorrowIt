import React, { useState } from 'react';
import PropTypes from 'prop-types';

const statuses = ['APPROVED', 'REJECTED', 'RETURNED', 'OVERDUE', 'CANCELLED', 'PENDING'];

const RentItem = ({ initialStartDate, initialEndDate, status, onSubmit, onCancel }) => {
    const [startDate, setStartDate] = useState(initialStartDate || '');
    const [endDate, setEndDate] = useState(initialEndDate || '');

    const handleSubmit = (e) => {
        e.preventDefault();

        const adjustedStartDate = new Date(startDate);
        adjustedStartDate.setHours(0, 0, 0, 0); // Set to the start of the day

        const adjustedEndDate = new Date(endDate);
        adjustedEndDate.setHours(0, 0, 0, 0); // Set to the start of the day

        if (adjustedStartDate > adjustedEndDate) {
            alert('Start date cannot be after end date');
            return;
        }

        if (adjustedStartDate < new Date().setHours(0, 0, 0, 0)) {
            alert('Start date cannot be in the past');
            return;
        }

        const rentDetails = {
            startDate: adjustedStartDate.toISOString(),
            endDate: adjustedEndDate.toISOString(),
            status,
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
    status: PropTypes.oneOf(statuses),
    onSubmit: PropTypes.func.isRequired,
    onCancel: PropTypes.func.isRequired,
};

export default RentItem;