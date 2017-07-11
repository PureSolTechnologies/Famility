import * as React from 'react';

export default class Icon extends React.Component<any, undefined> {

    static propTypes = {
        large: React.PropTypes.bool,
        name: React.PropTypes.string.isRequired
    };

    constructor( props: any ) {
        super( props );
    }

    render(): any {
        let path: string;
        if ( this.props.large ) {
            path = "/images/icons/FatCow_Icons32x32/" + this.props.name + ".png";
        } else {
            path = "/images/icons/FatCow_Icons16x16/" + this.props.name + ".png";
        }
        return <img src={path} />;
    }
}
