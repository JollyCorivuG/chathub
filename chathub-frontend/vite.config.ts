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
	}
})
