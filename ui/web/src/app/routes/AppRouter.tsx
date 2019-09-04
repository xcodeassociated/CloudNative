import React, {ReactChild, Component} from "react";
import { Switch, Route } from 'react-router-dom'
import Home from "../components/Home";
import About from "../components/About";
import LoginFormProvider from "../components/LoginFormProvider";
import PageNotFound from "../components/PageNotFound";
import {Nav} from "react-bootstrap";
import {Link, Router} from 'react-router-dom';
import history from './history';
import '../style/AppRouter.css';

type NamedProps = {
    data?: any
}

type Props = {
    children: ReactChild | NamedProps
}

class AppRouter extends Component<Props> {

    public render() {
        return(
            <Router history={history}>
                <div id="app-router">
                    <div id="menu" className="menu-bar">
                        <Nav defaultActiveKey="/home" as="ul" className="navbar-component">
                            <Nav.Item as="li" className="active">
                                <Link to="/">Home</Link>
                            </Nav.Item>
                            <Nav.Item as="li">
                                <Link to="/login">Login</Link>
                            </Nav.Item>
                            <Nav.Item as="li">
                                <Link to="/about">About</Link>
                            </Nav.Item>
                        </Nav>
                    </div>
                    <div id="body">
                        <Switch>
                            <Route exact path='/' component={Home}/>
                            <Route exact path='/about' component={About}/>
                            <Route path='/login' component={
                                () => <LoginFormProvider>{this.props.children}</LoginFormProvider>
                            } />
                            <Route component={PageNotFound}/>
                        </Switch>
                    </div>
                </div>
            </Router>
        )
    }
}

export default AppRouter;