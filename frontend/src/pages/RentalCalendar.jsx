import React, { useState, useEffect } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import '../styles/RentalCalendar.css';

const RentalCalendar = ({ existingRentals, onDateRangeSelect }) => {
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [validSelection, setValidSelection] = useState(true);

    useEffect(() => {
        validateDateRange(startDate, endDate);
    }, [startDate, endDate]);

    const isDateUnavailable = (date) => {
        if (!date) return false;

        const checkDate = new Date(date);
        checkDate.setHours(12, 0, 0, 0);

        return existingRentals.some(rental => {
            const rentalStart = new Date(rental.startDate);
            rentalStart.setHours(12, 0, 0, 0);

            const rentalEnd = new Date(rental.endDate);
            rentalEnd.setHours(12, 0, 0, 0);

            return checkDate >= rentalStart && checkDate <= rentalEnd;
        });
    };

    const filterUnavailableDates = (date) => {
        return !isDateUnavailable(date);
    };

    const highlightWithRanges = () => {
        const highlights = {};

        highlights["react-datepicker__day--highlighted-custom-unavailable"] =
            existingRentals.flatMap(rental => {
                return getDaysArray(
                    new Date(rental.startDate),
                    new Date(rental.endDate)
                );
            });

        if (startDate && endDate) {
            highlights["react-datepicker__day--highlighted-custom-selected"] =
                getDaysArray(startDate, endDate);
        }

        return highlights;
    };

    const getDaysArray = (start, end) => {
        const arr = [];
        const dt = new Date(start);
        dt.setHours(0, 0, 0, 0);
        const endCopy = new Date(end);
        endCopy.setHours(0, 0, 0, 0);

        while (dt <= endCopy) {
            arr.push(new Date(dt));
            dt.setDate(dt.getDate() + 1);
        }

        return arr;
    };

    const validateDateRange = (start, end) => {
        if (!start || !end) {
            setValidSelection(true);
            return true;
        }

        const daysInRange = getDaysArray(start, end);
        const isValid = !daysInRange.some(day => isDateUnavailable(day));

        setValidSelection(isValid);

        if (isValid) {
            onDateRangeSelect && onDateRangeSelect({
                startDate: start,
                endDate: end
            });
        } else {
            onDateRangeSelect && onDateRangeSelect(null);
        }

        return isValid;
    };

    const handleStartDateChange = (date) => {
        setStartDate(date);

        if (endDate && date > endDate) {
            setEndDate(null);
        }
    };

    const handleEndDateChange = (date) => {
        setEndDate(date);
    };

    return (
        <div className="rental-calendar-container">
            <h3>Select Rental Period</h3>

            <div className="date-pickers">
                <div className="date-picker-container">
                    <label>Start Date:</label>
                    <DatePicker
                        selected={startDate}
                        onChange={handleStartDateChange}
                        selectsStart
                        startDate={startDate}
                        endDate={endDate}
                        minDate={new Date()} // Can't select dates in the past
                        filterDate={filterUnavailableDates}
                        highlightDates={highlightWithRanges()}
                        placeholderText="Select start date"
                        className={!validSelection ? "invalid-date" : ""}
                     showMonthYearDropdown/>
                </div>

                <div className="date-picker-container">
                    <label>End Date:</label>
                    <DatePicker
                        selected={endDate}
                        onChange={handleEndDateChange}
                        selectsEnd
                        startDate={startDate}
                        endDate={endDate}
                        minDate={startDate || new Date()}
                        filterDate={filterUnavailableDates}
                        highlightDates={highlightWithRanges()}
                        placeholderText="Select end date"
                        className={!validSelection ? "invalid-date" : ""}
                        disabled={!startDate}
                     showMonthYearDropdown/>
                </div>
            </div>

            {!validSelection && (
                <div className="validation-error">
                    Selected dates overlap with existing rentals. Please select a different range.
                </div>
            )}
        </div>
    );
};

export default RentalCalendar;