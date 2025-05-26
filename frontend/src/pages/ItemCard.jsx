import {useNavigate} from "react-router-dom";

const ItemCard = ({ item, type, onDelete}) => {
    const navigate = useNavigate();

    const handleCardClick = () => {
        navigate(`/view-item/${item.id}`);
    };
    return (
        <div className="item-card" onClick={handleCardClick}>
            <img
                src={item.images && item.images.length > 0
                    ? "data:image/png;base64, " + item.images[0]
                    : " /no_image.jpeg"}
                alt={item.title || "Item"}
                className="item-image"
            />
            <div className="item-details">
                <h3>{item.title}</h3>
                <p>{item.description}</p>
                <div className="item-status">
                    Status: <span>{item.status || (type === 'lent' ? 'Lent' : 'Available')}</span>
                </div>

                {type === 'lent' ? (
                    <>
                        {onDelete && (
                            <button
                                onClick={() => onDelete(item.id)}
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

