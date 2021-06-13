import React, {Component} from "react";
import Accordion from "./Accordion";
import axios from "axios";

class BrowseProducts extends Component {

    constructor(props) {
        super(props);
        this.state = {
            accordionData: []
        };

        this.fetchData = this.fetchData.bind(this);
    }

    componentDidMount() {
        this.fetchData();
    }

    fetchData() {
        console.log("fetchData");

        axios.get('http://localhost:8080/v1/products')
            .then(res => {
                let acc = [];
                res.data.map(data => {
                    acc.push({
                        title: data.productId,
                        content: {
                            description: data.description,
                            storageTemp: data.storageTemperature,
                            height: data.heightInMillimeters,
                            width: data.widthInMillimeters,
                            length: data.lengthInMillimeters,
                            weight: data.weightInKilograms,
                            barcodes: data.barcodes,
                            imageUrl: data.imageUrl
                        }
                    })
                })
                this.setState({accordionData: acc});
            })
    }

    render() {
        return (
            <div>
                <h2>Browse Product Details</h2>
                <p>Use elements below to access products data</p>

                <button onClick={this.fetchData}>Reload Data</button>
                <Accordion data={this.state.accordionData}/>
            </div>
        );
    }
}

export default BrowseProducts;
