import React, { Component } from 'react';
import configureStore from '../store/config/storeConfig'
import AppContextProvider from "../context/AppContextProvider";
import '../style/App.css';

class App extends Component {

  public store = (): object => {
    const store = Object.assign({}, this.props);
    return configureStore(store);
  };

  public render(): JSX.Element {
    return (
        <div id="app">
          <AppContextProvider children={this.store()} />
        </div>
    );

  }
}

export default App;
