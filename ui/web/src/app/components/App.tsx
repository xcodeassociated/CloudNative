import React, { Component } from 'react';
import configureStore from '../store/config/storeConfig'
import AppRouter from "../routes/AppRouter";
import '../style/App.css';

class App extends Component {

  public store = (): object => {
    const store = Object.assign({}, this.props);
    return configureStore(store);
  };

  public render(): JSX.Element {
    return (
        <div id="app">
          <AppRouter>{this.store()}</AppRouter>
        </div>
    );

  }
}

export default App;
