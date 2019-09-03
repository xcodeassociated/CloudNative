import React, {Component} from "react";
import {Nav} from "react-bootstrap";
import {Link} from 'react-router-dom';
import '../style/NavBar.css';

class NavBar extends Component {

    public render() {
        return(
            <Nav defaultActiveKey="/home" as="ul" className="navbar-header">
                <Nav.Item as="li">
                    <Link to="/">Home</Link>
                </Nav.Item>
                <Nav.Item as="li">
                    <Link to="/login">Login</Link>
                </Nav.Item>
            </Nav>
        )
    }
}

export default NavBar;