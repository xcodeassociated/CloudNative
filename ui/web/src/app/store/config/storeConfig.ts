import {applyMiddleware, createStore} from 'redux';
import thunk from 'redux-thunk';
import rootReducer from '../reducers/rootReducer'

const middleware: any[] = [];

middleware.push(thunk);

export default function configureStore(initialState) {
  return createStore(
    rootReducer,
    initialState,
    applyMiddleware(...middleware)
  );
}
