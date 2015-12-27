package  com.ly.friend.service;

import com.ly.friend.vo.Contact;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.service.IdEntityService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.nutz.dao.Cnd;
import com.ly.comm.Page;

import java.util.List;


@IocBean(fields = { "dao" })
public class ContactService extends IdEntityService<Contact> {

	public static String CACHE_NAME = "contact";
    public static String CACHE_COUNT_KEY = "contact_count";

    public List<Contact> queryCache(Cnd c,Page p)
    {
        List<Contact> list_contact = null;
        String cacheKey = "contact_list_" + p.getPageCurrent();

        Cache cache = CacheManager.getInstance().getCache(CACHE_NAME);
        if(cache.get(cacheKey) == null)
        {
            list_contact = this.query(c, p);
            cache.put(new Element(cacheKey, list_contact));
        }else{
            list_contact = (List<Contact>)cache.get(cacheKey).getObjectValue();
        }
        return list_contact;
    }

    public int listCount(Cnd c)
    {
        Long num = 0L;
        Cache cache = CacheManager.getInstance().getCache(CACHE_NAME);
        if(cache.get(CACHE_COUNT_KEY) == null)
        {
            num = Long.valueOf(this.count(c));
            cache.put(new Element(CACHE_COUNT_KEY, num));
        }else{
            num = (Long)cache.get(CACHE_COUNT_KEY).getObjectValue();
        }
        return num.intValue();
    }



}


