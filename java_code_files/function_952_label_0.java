    public R update(@Validated FriendLinkDO friendLink) {
        friendLinkService.update(friendLink);
        redisTemplate.delete(CacheKey.INDEX_LINK_KEY);
        return R.ok();
    }