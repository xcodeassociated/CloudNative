import { applyMiddleware, compose, createStore } from 'redux';
import { logger } from 'redux-logger';
import thunk from 'redux-thunk';
import rootReducer from '../reducers/root_reducer'

const middleware: any[] = [];

middleware.push(logger);
middleware.push(thunk);

export default function configureStore(initialState) {
  return createStore(
    rootReducer,
    initialState,
    applyMiddleware(...middleware)
  );
}
