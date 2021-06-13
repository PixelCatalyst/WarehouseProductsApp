import React from "react";
import AccordionItem from "./AccordionItem";

const Accordion = ({data}) => {

    return (
        <div className="accordion">
            {data.map(({title, content}) => (
                <AccordionItem title={title} content={content}/>
            ))}
        </div>
    );
}

export default Accordion;
