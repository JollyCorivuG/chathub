import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite'
import { VantResolver } from 'unplugin-vue-components/resolvers'
import path from 'path'
export default defineConfig({
  	plugins: [
	    vue(),
		Components({
			resolvers: [VantResolver()],
		}),
	],
	resolve: {
		alias: {
			'@': path.resolve(__dirname, 'src')
		}
	},
	// 配置代理跨域
	server: {
		proxy: {
			'/api': {
				target: 'http://localhost:8081',
				changeOrigin: true,
			}
		}
	}
})
