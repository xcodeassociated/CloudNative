import React, {Component} from "react";
import {Nav, Navbar} from "react-bootstrap";


class NavBar extends Component {

    public render() {
        return(
            <Navbar bg="light" expand="lg">
                <Navbar.Brand href="/">Web</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="mr-auto">
                        <Nav.Link href="/">Home</Nav.Link>
                        <Nav.Link href="/login">Login</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        )
    }
}

export default NavBar;