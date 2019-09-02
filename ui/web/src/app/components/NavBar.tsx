import React, {Component} from "react";
import {Nav, Navbar} from "react-bootstrap";
import {Link} from 'react-router-dom';


class NavBar extends Component {

    public render() {
        return(
            <Navbar bg="light" expand="lg">
                <Navbar.Brand href="/">Web</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="mr-auto">
                        <Link to="/">Home</Link>
                        <Link to="/login">Login</Link>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        )
    }
}

export default NavBar;