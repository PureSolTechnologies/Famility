const {resolve} = require('path');
const webpack = require('webpack');
const validate = require('webpack-validator');
const {getIfUtils, removeEmpty} = require('webpack-config-utils');

module.exports = env => {
  const {ifProd, ifNotProd} = getIfUtils(env)

  return validate({
    entry: ['./js/main.jsx'],
    context: __dirname,
    output: {
      path: resolve(__dirname, './build'),
      filename: 'bundle.js',
      publicPath: '/build/',
      pathinfo: ifNotProd(),
    },
    resolve: {
        extensions: [".tsx", ".ts", ".jsx", ".js"]
    },
    devtool: ifNotProd('source-map', 'eval'),
    devServer: {
      port: 9090,
      historyApiFallback: true
    },
    module: {
      loaders: [
        {test: /\.jsx$/, exclude: /node_modules/, loader: 'babel-loader'},
        {test: /\.js$/, exclude: /node_modules/, loader: 'babel-loader'},
        {test: /\.css$/, loader: 'style-loader!css-loader'},
        {test: /(\.eot|\.woff2|\.woff|\.ttf|\.svg)$/, loader: 'file-loader'},
        {test: /\.tsx?$/, exclude: /node_modules/, loader: 'ts-loader'},
      ],
    },
    plugins: removeEmpty([
      ifProd(new webpack.LoaderOptionsPlugin({
        minimize: true,
        debug: false,
        quiet: true,
      })),
      ifProd(new webpack.DefinePlugin({
        'process.env': {
          NODE_ENV: '"production"',
        },
      })),
      ifProd(new webpack.optimize.UglifyJsPlugin({
        sourceMap: true,
        compress: {
          screw_ie8: true, // eslint-disable-line
          warnings: false,
        },
      })),
    ]),
  });
};
