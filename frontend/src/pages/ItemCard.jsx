import { useNavigate } from "react-router-dom";
import '../styles/ItemCard.css'

const ItemCard = ({ item, type, onDelete }) => {
    const navigate = useNavigate();

    const handleCardClick = () => {
        var id = item.id;
        if (type === "rented") {
            id = item.itemId;
            navigate(`/view-item/${id}?type=${type}`, {
                state: {
                    rentalInfo: {
                        rentalId: item.id,
                        renterName: item.renterName || "Unknown",
                        startDate: item.startDate,
                        endDate: item.endDate,
                    }
                }
            });
        } else {
            navigate(`/view-item/${id}?type=${type}`);
        }
    };

    const formatDate = (dateString) => {
        const options = { year: "numeric", month: "long", day: "numeric", hour: "2-digit", minute: "2-digit" };
        const date = new Date(dateString);
        return new Intl.DateTimeFormat("en-US", options).format(date);
    };

    return (
        <div className="item-card" onClick={handleCardClick}>
            <img
                src={item.images && item.images.length > 0
                    ? "data:image/png;base64," + item.images[0]
                    : "/no_image.jpeg"}
                alt={item.title || "Item"}
                className="item-image"
            />
            <div className="item-details">
                <h3>{item.title}</h3>
                <p>{item.description}</p>
                <div className="item-status">
                    Status: <span>{item.status || (type === "lent" ? "Lent" : "Available")}</span>
                </div>

                {/* Show rental period for rented items */}
                {type === "rented" && item.startDate && item.endDate && (
                    <div className="item-rental-period">
                        <span>Rental Period: </span>
                        <span>
                            {formatDate(item.startDate)} to {formatDate(item.endDate)}
                        </span>
                    </div>
                )}

                {type === "owner" ? (
                    <>
                        {onDelete && (
                            <button
                                onClick={(e) => {
                                    e.stopPropagation(); // Prevent navigation on button click
                                    onDelete(item.id);
                                }}
                                className="delete-button"
                            >
                                Remove Item
                            </button>
                        )}
                    </>
                ) : (
                    <div className="item-owner">
                        Owner: <span>{item.owner}</span>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ItemCard;