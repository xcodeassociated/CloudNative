import React, {Component} from "react";

interface IHomeProps {
    param?: string;
}

class Home extends Component<IHomeProps> {

    public render() {
        return(
            <div id="home">
                <p>Home Page</p>
                {this.props.param !== undefined ? <p>Param: {this.props.param}</p> : null}
            </div>
        )
    }
}

export default Home;