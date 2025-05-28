import React, {useEffect, useState} from 'react';
import PropTypes from 'prop-types';
import RentalCalendar from './RentalCalendar';
import axios from "axios";

const statuses = ['RETURNED', 'OVERDUE', 'RENTED', 'CANCELLED', 'MARKED_AS_RETURNED'];

const RentItem = ({ itemId, initialStartDate, initialEndDate, status, onSubmit, onCancel }) => {
    const [startDate, setStartDate] = useState(initialStartDate || '');
    const [endDate, setEndDate] = useState(initialEndDate || '');
    const [existingRentals, setExistingRentals] = useState([]);

    useEffect(() => {
        getExistingRentalDates(itemId);
    }, [itemId]);

    function setSelectedDateRange(range) {
        if (range === null) {
            return;
        }
        setStartDate(range.startDate);
        setEndDate(range.endDate);
    }

    const handleDateRangeSelect = (range) => {
        setSelectedDateRange(range);
    };

    const getExistingRentalDates = async () => {
        try {
            const response = await axios.get(`/rentals/item/${itemId}`, {
                headers:
                    {
                        "Authorization": `${localStorage.getItem('authToken')}`,
                    }
            });
            setExistingRentals(response.data);
        } catch
            (error)
            {
                console.error("Error fetching existing rental dates:", error);
            }
        }

        const handleSubmit = (e) => {
            e.preventDefault();

            const adjustedStartDate = new Date(startDate);
            adjustedStartDate.setHours(12, 0, 0, 0);

            const adjustedEndDate = new Date(endDate);
            adjustedEndDate.setHours(12, 0, 0, 0);

            console.log(adjustedStartDate.toISOString());
            console.log(adjustedEndDate.toISOString());
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
                    <RentalCalendar existingRentals={existingRentals} onDateRangeSelect={handleDateRangeSelect}/>
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