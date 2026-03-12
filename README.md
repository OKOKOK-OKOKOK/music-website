<h1 align="center">music-website</h1>

## 项目预览

> 前台截图预览

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gjdm8x3jj21c00u00ui.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gje55hgxj21c00u0n3v.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gk5fxmwxj21c00u0wm2.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gk5rtelgj21c00u00w7.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gk6a1b8wj21c00u0tf2.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gkl6bu35j21c00u00wb.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gklntw77j21c00u077j.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gkokestbj21c00u0ju8.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gknhg12sj21c00u00v4.jpg)

<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gknu0rszj21c00u0jto.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gkoxoehnj21c00u0q5j.jpg)<br/>

> 后台截图预览

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h158xvsdvij21c00u0wi8.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h159x0re56j21c00u077a.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h159xzbi85j21c00u0whn.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h159zewsh4j21c00u079f.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h159yz5x8hj21c00u0win.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h159yo2nzmj21c00u0djp.jpg)<br/>

## 项目功能

- 音乐播放
- 用户登录注册
- 用户信息编辑、头像修改
- 歌曲、歌单搜索
- 歌单打分
- 歌单、歌曲评论
- 歌单列表、歌手列表分页显示
- 歌词同步显示
- 音乐收藏、下载、拖动控制、音量控制
- 后台对用户、歌曲、歌手、歌单信息的管理

<br/>

## 技术栈

### 后端

**SpringBoot + MyBatis + Redis** **+ minio**

### 部署

**docker**

### 前端

**Vue3.0 + TypeScript + Vue-Router + Vuex + Axios + ElementPlus + Echarts**

<br/>


## 下载运行

### 1、下载项目到本地

```bash
git clone git@github.com:Yin-Hongwei/music-website.git

# 上面下载慢可以用下面这个
git clone git@gitee.com:Yin-hongwei/music-website.git
```

### 2、数据库
<img src="https://tva1.sinaimg.cn/large/e6c9d24ely1h6gz1le9wxj20fo0gggmh.jpg" height="200px"/>

### 3、修改配置文件

1）创建数据库
将 `music-website/music-server/sql` 文件夹中的 `tp_music.sql` 文件导入数据库。

2）修改用户名密码
修改 `music-website/music-server/src/main/resources/application.properties` 文件里的 `spring.datasource.username` 和 `spring.datasource.password`；

### 4、启动项目

- **启动管理端**：进入 music-server 文件夹，运行下面命令启动服务器

```js
./mvnw spring-boot:run

mvn spring-boot:run // 前提装了 maven
```

- **启动 redis**：直接在终端输入下面命令

```
redis-server
```

- **启动客户端**

```js
npm install // 安装依赖

npm run serve // 启动前台项目
```

- **启动管理端**：进入 music-manage 目录，运行下面命令

```js
npm install // 安装依赖

npm run serve // 启动后台管理项目
```

