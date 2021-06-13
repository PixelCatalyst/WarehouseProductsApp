import React, {Component} from "react";
import {BrowserRouter, NavLink, Route} from "react-router-dom";
import BrowseProducts from "./BrowseProducts";
import About from "./About";

class Main extends Component {
    render() {
        return (
            <BrowserRouter>
                <div>
                    <h1>Warehouse Products</h1>
                    <ul className="header">
                        <li><NavLink exact to="/">Browse Products</NavLink></li>
                        <li><NavLink to="/about">About</NavLink></li>
                    </ul>
                    <div className="content">
                        <Route path="/" exact component={BrowseProducts}/>
                        <Route path="/about" exact component={About}/>
                    </div>
                </div>
            </BrowserRouter>
        );
    }
}

export default Main;
