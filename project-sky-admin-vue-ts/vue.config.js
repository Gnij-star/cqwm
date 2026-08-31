const path = require('path')
const name = 'Vue Typescript Admin'
const IS_PROD = ['production', 'development'].includes(process.env.NODE_ENV)

module.exports = {
  'publicPath': process.env.NODE_ENV === 'production' ? './' : '/',
  'lintOnSave': false, // 关闭 ESLint，避免 eslint 模块找不到的问题
  'pwa': {
    'name': name
  },
  'pluginOptions': {
    'style-resources-loader': {
      'preProcessor': 'scss',
      'patterns': [
        path.resolve(__dirname, 'src/styles/_variables.scss'),
        path.resolve(__dirname, 'src/styles/_mixins.scss')
      ]
    }
  },
  devServer: {
    host:'0.0.0.0',
    public: '0.0.0.0:8888',
    port: 8888,
    open: true,
    disableHostCheck:true,
    hot:true,
    overlay: {
      warnings: false,
      errors: true
    },
    proxy: {
      '/api': {
        target: process.env.VUE_APP_URL || 'http://localhost:8088',
        ws: false,
        secure: false,
        changeOrigin: true
      }
    }
  },
  chainWebpack: (config) => {
    config.resolve.symlinks(true)
    
    // ✅ 彻底移除 fibers
    const sassRule = config.module.rule('scss')
    sassRule.uses.delete('fibers')
    
    const sassRule2 = config.module.rule('sass')
    sassRule2.uses.delete('fibers')
    
    // ✅ 禁用 ESLint 检查（解决 eslint 模块找不到的问题）
    config.module.rules.delete('eslint')
  },
  configureWebpack: {
    devtool: 'source-map'
  },
  css: {
    extract: IS_PROD,
    sourceMap: false,
    loaderOptions: {},
    modules: false,
  },
}