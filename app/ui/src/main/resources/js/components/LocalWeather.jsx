import React from 'react';

export default class LocalWeather extends React.Component {

    constructor( props ) {
        super( props );
    }

    componentDidMount() {
        _wcomWidget( { id: 'wcom-189aa4fb05d88ee61654a2d0315295cd', location: 'DE0002265247', format: '300x250', type: 'summary' });
    }

    render() {
        return <div id="wcom-189aa4fb05d88ee61654a2d0315295cd" className="wcom-default w300x250" style={{'border': '1px solid #CCC', 'background-color': '#FCFCFC', 'border-radius': '5px'}}>
            <link rel="stylesheet" href="//static1.wetter.com/woys/5/css/w.css" media="all" />
            <div className="wcom-city">
                <a style={{'color': '#000'}} href="http://www.wetter.com/deutschland/dresden/weissig/DE0002265247.html" target="_blank" rel="nofollow" title="Wetter Weißig">Wetter Weißig</a>
            </div>
            <div id="wcom-189aa4fb05d88ee61654a2d0315295cd-weather">
            </div>
        </div>;
    }

}