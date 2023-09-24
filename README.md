# Video-Transcoding-Server

> 기본 동영상 파일을 웹에서 스트리밍하도록 동영상 인코딩하는 서버


## 트랜스코딩 서버 API

### 트랜스코딩 요청
```
URL : /api/video/transcoding
Request Body : 

{
  "filePath" : "/video/{channel-id}/{video-id}/original/{file-name}",
  "resolutions" : [
    {
      "protocol" : "hls",
      "resolutions" : [1080, 720, 480]
    },
    {
      "protocol" : "dash",
      "resolutions" : [720, 480]
    }
  ]
}
```

### 트랜스코딩 성공시
```
Status Code : 200 OK
Response Body : 

{
  "resolutions": [
    {
      "protocol": "hls",
      "isCompleted": true
    },
    {
      "protocol": "dash",
      "isCompleted": true
    }
  ]
}
```

### 트랜스코딩 실패시
```
Status Code : 500 Internal Server Error
Response Body : 

{
  "resolutions": [
    {
      "protocol": "hls",
      "isCompleted": true
    },
    {
      "protocol": "dash",
      "isCompleted": false
    }
  ]
}
```
--------------------------------
### 썸네일 추출 요청
```
URL : /api/video/thumbnail
Request Body :

{
  "filePath" : "/video/{channel-id}/{video-id}/original/{file-name}"
}
```

### 썸네일 추출 성공시
```
Status Code : 200 OK
Response Body :

{
  "isCompleted" : true
}
```

### 썸네일 추출 실패시
```
Status Code : 500 Internal Server Error
Response Body :

{
  "isCompleted" : false
}
```
