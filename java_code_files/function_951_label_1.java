    public R save(FriendLinkDO friendLink) {
        if (friendLinkService.save(friendLink) > 0) {
            redisTemplate.delete(CacheKey.INDEX_LINK_KEY);
            return R.ok();
        }
        return R.error();
    }