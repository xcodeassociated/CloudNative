import React, {Component, ReactChild} from "react";
import { Provider } from 'react-redux';
import AppRouter from "./AppRouter";

type NamedProps = {
    data?: any
}

type Props = {
    children: ReactChild | NamedProps
}

class AppRouterProvider extends Component<Props> {

    public render() {
        return(
            <Provider store={this.props.children}>
                <AppRouter children={this.props.children} />
            </Provider >
        )
    }
}

export default AppRouterProvider;