import React, {Component, ReactChild} from "react";
import Logout from "./Logout";
import {Provider} from 'react-redux';

type NamedProps = {
  data?: any
}

type Props = {
  children: ReactChild | NamedProps
}

class LogoutProvider extends Component<Props> {

  public render() {
    return (
      <Provider store={this.props.children}>
        <Logout/>
      </Provider>
    )
  }
}

export default LogoutProvider;
