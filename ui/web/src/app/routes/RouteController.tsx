import React, {ReactChild, Component} from "react";
import { Switch, Route } from 'react-router-dom'
import Home from "../components/Home";
import LoginFormProvider from "../components/LoginFormProvider";

type NamedProps = {
    data?: any
}

type Props = {
    children: ReactChild | NamedProps
}

class RouteController extends Component<Props> {

    public render() {
        return(
            <Switch>
                <Route exact path='/' component={Home}/>
                <Route path='/login' component={
                    () => <LoginFormProvider>{this.props.children}</LoginFormProvider>
                } />
            </Switch>
        )
    }
}

export default RouteController;