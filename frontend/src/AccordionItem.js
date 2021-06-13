import React, {useState} from "react";

const AccordionItem = ({title, content}) => {

    const [isActive, setIsActive] = useState(false);

    return (
        <React.Fragment>
            <div className="accordion-item">
                <div
                    className="accordion-title"
                    onClick={() => setIsActive(!isActive)}
                >
                    <div>{title}</div>
                    <div>{isActive ? '-' : '+'}</div>
                </div>
                {isActive && <div className="accordion-content">
                    <img srcSet={content.imageUrl} src={content.imageUrl} height={"200px"} width={"200px"} alt={content.description}/>
                    <ul>
                        <li><b>Description:</b> {content.description}</li>
                        <li><b>Storage temperature:</b> {content.storageTemp}</li>
                        <li><b>Physical dimensions:</b>
                            <ul>
                                <li><i>height(mm):</i> {content.height}</li>
                                <li><i>width(mm):</i> {content.width}</li>
                                <li><i>length(mm):</i> {content.length}</li>
                            </ul>
                        </li>
                        <li><b>Weight(kg):</b> {content.weight}</li>
                        {content.barcodes.length > 0 && <li><b>Barcodes:</b>
                            <ul>
                                {content.barcodes.map(function (item, i) {
                                    return <li key={i}>{content.barcodes[i]}</li>;
                                })}
                            </ul>
                        </li>}
                    </ul>
                </div>}
            </div>
        </React.Fragment>
    );
};

export default AccordionItem;
