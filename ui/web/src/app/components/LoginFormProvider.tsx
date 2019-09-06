import React, {Component, ReactChild} from "react";
import Form from "./LoginForm";
import {Provider} from 'react-redux';

type NamedProps = {
  data?: any
}

type Props = {
  children: ReactChild | NamedProps
}

class LoginFormProvider extends Component<Props> {

  public render() {
    return (
      <Provider store={this.props.children}>
        <Form/>
      </Provider>
    )
  }
}

export default LoginFormProvider;
