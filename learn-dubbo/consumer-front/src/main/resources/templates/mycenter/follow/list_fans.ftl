
<style type="text/css">
    #active1{
        color:black;
        font-weight: bold;
    }
</style>
<style>
    #p_active2{  display: none;  }
    #p_active3{  display: none;  }
    #p_active4{  display: none;  }
    #p_active5{  display: none;  }
    #p_active6{  display: none;  }
    #p_active7{  display: none;  }
</style>

<#include  "../base/header.ftl" >
<script src="${base}/front/js/jquery.min.js"></script>
<link rel="stylesheet" href="${base}/mycenter/css/follow.css" type="text/css" />
<!-- main container -->
<div class="content">
    <div class="bg-other user-head-info">
        <div class="user-info clearfix">
            <div class="user-pic" data-is-fans="" data-is-follows="">

            </div>



        </div><!-- .user-info end -->
    </div>
    <div class="wrap">
        <div class="u-container">
            <div class="c-tab clearfix">
                <div class="tool-left l">
                <a href="${base}/follow/listAttention?user1Id=${(userFront.id)!}" class="sort-item">Ta的关注</a>
                <a href="${base}/follow/listFans?user2Id=${(userFront.id)!}" class="sort-item active">Ta的粉丝</a>
                </div>
            </div>
            <div class="concern-list">
                <ul>
                    <#if fansList?exists>
                    <#list fansList as fans>
                    <li class="box">
                        <div class="left-img">
                            <a href="https://www.imooc.com/u/6213439" target="_blank"><img src="${base}/upload/${(fans.user1.headimg)!}" class="top_head"></a>
                        </div>
                        <div class="right-c">
                            <div class="title">
                                <a href="https://www.imooc.com/u/6213439" target="_blank"><span class="nickname">${(fans.user1.username)!}</span></a>
                                <ul class="fa fa-user"></ul>
                            </div>
                            <p class="desc" title="学生"><#if "${(fans.user1.userType)!}"=="1">学生<#elseif "${(fans.user1.userType)!}"=="2">讲师<#else>管理员</#if></p>
                            <div class="fs-line">
                                <a href="https://www.imooc.com/u/6213439/follows" class="u-target"><span class="group">${(fans.user1.city)!}</span></a>
                                <a href="https://www.simooc.com/u/6213439/fans" class="u-target u-margin-l-15">${(fans.user1.position)!}</span></a>
                            </div>
                            <div class="btn-line" data-is_self="0" data-is-fans="0">
                                <a href="javascript:" data-uid="6213439" class="btn-o btn-gray-o js-concern-already">已被关注</a>
                                <#if attentionList?exists>
                                <#assign count =1 />
                                <#list attentionList as attention>
                                <#if attention.user2.id == fans.user1.id>
                                    <a href="javascript:" data-uid="6213439" class="btn-o btn-gray-o js-concern-already">已关注</a>
                                    <#break>
                                </#if>
                                    <#assign  count =count +1 />
                                </#list>
                                <#--<#assign count =count +1 />-->
                                <#--${count}..${attentionList?size}-->
                                    <#if count gt attentionList?size>
                                        <a onclick="addAttention(${fans.user1.id})" class="btn-o btn-green-o  js-concern-follow">关注</a>
                                    </#if>
                                </#if>
                            </div>

                    </li>
                    </#list>
                        </#if>
                        <script>
                            function addAttention(id){
                                $.ajax({
                                    url: "/follow/addAttention?userId="+id,
                                    type: "GET",
                                    cache: false,
                                    success: function(data) {
                                        if (data == "success") {
                                            alert("关注成功");
                                            window.location.reload();
                                        }else{
                                            alert("关注失败");
                                            window.location.reload();
                                        }
                                    },
                                    complete: function() {
//                                            alert("complete");
                                    }
                                });
                            }
                        </script>
                    <#--<li class="box">-->
                        <#--<div class="left-img">-->
                            <#--<a href="https://www.imooc.com/u/2750705" target="_blank"><img src="onemoo%E7%9A%84%E5%85%B3%E6%B3%A8_files/5458640c0001b0a702200220-100-100.jpg" class="top_head"></a>-->
                        <#--</div>-->
                        <#--<div class="right-c">-->
                            <#--<div class="title">-->
                                <#--<a href="https://www.imooc.com/u/2750705" target="_blank"><span class="nickname">2016rrrrrr</span></a>-->
                                <#--<ul class="icon-list"></ul>-->
                            <#--</div>-->
                            <#--<p class="desc" title="学生">学生</p>-->
                            <#--<div class="fs-line">-->
                                <#--<a href="https://www.imooc.com/u/2750705/follows" class="u-target"><span class="group"><em>关注</em><em class="u-margin-l-5">11</em></span></a>-->
                                <#--<a href="https://www.imooc.com/u/2750705/fans" class="u-target u-margin-l-15"><span class="group"><em>粉丝</em><em class="u-margin-l-5">11</em></span></a>-->
                            <#--</div>-->
                            <#--<div class="btn-line" data-is_self="0" data-is-fans="0">-->
                                <#--<a href="javascript:" data-uid="2750705" class="btn-o btn-gray-o hide js-concern-mutual">互相关注</a>-->
                                <#--<a href="javascript:" data-uid="2750705" class="btn-o btn-gray-o hide js-concern-already">已关注</a>-->
                                <#--<a href="javascript:" data-uid="2750705" class="btn-o btn-green-o  js-concern-follow">关注</a>-->
                                <#--<a href="https://www.imooc.com/u/1328987/messages?uid=2750705" target="_blank" class="btn-o btn-gray-o hide js-concern-msg">私信</a>-->
                            <#--</div>-->
                    <#--</li>-->
                </ul>
            </div>
        </div>
    </div>
</div>
