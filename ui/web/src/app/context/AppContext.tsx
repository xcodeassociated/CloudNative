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
import { connect } from 'react-redux';
import { logoutAction } from '../store/actions/loginAction'
import Reservations from "../components/Reservations";

type NamedProps = {
    data?: any
}

type PropsAppRouter = {
    dispatch: Function
    children: ReactChild | NamedProps
}

interface IStateAppRouter {}

class AppContext extends Component<PropsAppRouter, IStateAppRouter> {

    constructor(props: Readonly<PropsAppRouter>) {
        super(props);
        this.state = {};
    }

    public isLoggedIn(): boolean {
        return localStorage.getItem('token') != null;
    };

    public render() {
        if(localStorage.getItem('token') != null) {
            console.log(">>> logged in: " + localStorage.getItem('token'));
        }

        return(
        <Router history={history}>
            <div id="app-router">
                <div id="menu" className="menu-bar">
                    <Nav defaultActiveKey="/home" as="ul" className="navbar-component">
                        <Nav.Item as="li" className="active">
                            <Link to="/">Home</Link>
                        </Nav.Item>
                        {!this.isLoggedIn() ?
                            <Nav.Item as="li">
                                <Link to="/login">Login</Link>
                            </Nav.Item>
                            :
                            <Nav.Item as="li">
                                <Link to="/reservations">Reservations</Link>
                            </Nav.Item>
                        }
                        <Nav.Item as="li">
                            <Link to="/about">About</Link>
                        </Nav.Item>
                        {this.isLoggedIn() ?
                            <Nav.Item as="li">
                                <Link onClick={
                                    logoutAction()
                                } to="/">Logout</Link>
                            </Nav.Item> : ""
                        }
                    </Nav>
                </div>
                <div id="body">
                    <Switch>
                        <Route exact path='/' component={Home}/>
                        <Route exact path='/reservations' component={Reservations}/>
                        <Route exact path='/about' component={About}/>
                        <Route path='/login' component={
                            () => <LoginFormProvider children={this.props.children} />
                        } />
                        <Route component={PageNotFound}/>
                    </Switch>
                </div>
            </div>
        </Router>
    )
}
}

const mapStateToPropsAppRouter = (state) => {
    return {
        isLoginPending: state.isLoginPending,
        isLoginSuccess: state.isLoginSuccess,
        loginError: state.loginError
    };
};

export default connect(mapStateToPropsAppRouter)(AppContext);
