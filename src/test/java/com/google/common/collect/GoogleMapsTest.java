package com.google.common.collect;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * TODO
 * Created by dongdaiming on 2018-07-09 13:59
 */
public class GoogleMapsTest {

    @Test
    public void testSetCompare() {
        Set<String> set1 = JSON.parseObject("[\"15026996221\",\"13761008456\",\"17887948547\",\"13636576797\",\"13917539779\",\"15036109882\",\"13075027362\",\"13122663088\",\"13472724889\",\"13020151262\",\"13262506569\",\"13601810565\",\"13127832169\",\"13162815900\",\"13636627601\",\"13564656054\",\"18616327900\",\"15801745176\",\"13044143056\",\"15026899387\",\"15221379917\",\"13761457041\",\"17317899653\",\"18721989813\",\"18301916147\",\"13044143817\",\"18621789360\",\"13818926747\",\"13044143457\",\"13044142662\",\"18930256609\",\"13044142101\",\"15821163324\",\"15958331267\",\"15824709589\",\"18656087114\",\"57382820276\",\"18721308535\",\"18001818001\",\"13585716055\",\"13044142693\",\"15800621955\",\"15068308225\",\"18521704151\",\"18516774639\",\"13764225711\",\"13661832442\",\"18516774036\",\"18516064699\",\"18217050358\"]", Set.class);
        Set<String> set2 = JSON.parseObject("[\"13061619668\",\"13668894753\",\"13774488413\",\"13162900395\",\"13944444444\",\"18721013049\",\"13636624668\",\"13764643399\",\"18001602286\",\"15021099773\",\"13901892674\",\"13472586835\",\"13901891100\",\"15618523657\",\"13817345027\",\"15026899387\",\"15800617505\",\"13766664444\",\"13122216518\",\"13916848114\",\"13331952306\",\"18918109377\",\"13913326799\",\"18918109969\",\"15709201129\",\"13761173877\",\"13020151262\",\"13816820836\",\"13917185920\",\"13817197770\",\"18966667777\",\"13818117175\",\"13655557777\",\"18916212157\",\"13917366832\",\"13761535030\",\"13636672116\",\"13052026013\",\"15821354475\",\"13816763868\",\"15521118746\",\"15000324535\",\"18918109194\",\"18260007125\",\"18918109196\",\"18501777332\",\"13564887886\",\"13916331918\",\"13918138871\",\"18616189637\",\"15201826579\",\"18621658156\",\"13402198557\",\"13916064531\",\"13472580130\",\"18116199827\",\"13816865545\",\"13917710087\",\"13524183808\",\"18898575816\",\"13615888872\",\"15852495228\",\"18621289076\",\"13816993527\",\"13918014877\",\"15300727582\",\"13817053568\",\"13701672776\",\"18901954927\",\"13636321904\",\"13901900982\",\"13382191008\",\"13061712039\",\"15271935001\",\"15900469808\",\"13917539779\",\"13636449484\",\"13482107810\",\"13761008456\",\"13916951641\",\"15355931476\",\"13651971702\",\"18917057566\",\"18918109177\",\"13761414118\",\"18321290847\",\"18930080928\",\"13817750438\",\"18601735792\",\"15601601141\",\"15026996221\",\"13681611941\",\"15026485626\",\"13661402002\",\"18321413627\",\"13127502638\",\"18116130405\",\"13636576797\",\"15221753517\",\"18621735025\",\"13162802997\",\"13524663308\",\"13611947567\",\"18616277399\",\"18661294280\",\"13564701102\",\"15221255182\",\"18516137300\",\"13817905738\",\"13601818443\",\"15021889479\",\"13978315535\",\"13951348401\",\"13075027362\",\"18121021102\",\"18930958126\",\"18611328239\",\"13818076559\",\"13916451755\",\"13917140607\"]", Set.class);
        set2.retainAll(set1);
        System.out.println(set2.size());
        System.out.println(set2);
    }

    @Test
    public void testMapCompare() {
        Map<Integer, Integer> map = new TreeMap<>();
        map.put(1, 11);
        map.put(2, 22);
        map.put(3, 33);
        map.put(4, 44);
        Map<Integer, Integer> map2 = new TreeMap<>();
        map2.put(1, 11);
        map2.put(2, 22);
        map2.put(33, 333);
        MapDifference<Integer, Integer> r = Maps.difference(map, map2);
        System.out.println("only left:" + JSON.toJSONString(r.entriesOnlyOnLeft()));
        System.out.println("only right:" + JSON.toJSONString(r.entriesOnlyOnRight()));
        System.out.println("commons:" + JSON.toJSONString(r.entriesInCommon()));
    }

    @Test
    public void testMultiMap() {
        Multimap<String, String> map = ArrayListMultimap.create();
        map.put("girl", "dwj");
        map.put("girl", "wqf");
        map.put("wife", "smart");
        System.out.println(map);
        Assert.assertEquals(3, map.size());
        Collection<String> girls = map.get("girl");
        System.out.println(girls);
        Assert.assertEquals(2, girls.size());
    }

    @Test
    public void testImmutableMap() {
        ImmutableMap<String, String> map;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        map = builder.put("k1", "v1").put("k2", "v2").build();
        System.out.println(map);

        ImmutableMap<String, String> map2 = ImmutableMap.of("k3", "v3", "k4", "v4");
        System.out.println(map2);
    }

    // BiMap value也要唯一，如果value重复则会抛异常
    @Test
    public void testBiMap() {
        BiMap<String, String> map = HashBiMap.create();
        map.put("k1", "v1");
        map.put("k1", "v2");
        Assert.assertEquals(1, map.size());

        BiMap<String, String> map2 = HashBiMap.create();
        map2.put("k1", "v1");
        try {
            map2.put("k2", "v1");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
            e.printStackTrace();
        }
        Assert.assertEquals(1, map2.size());

        Assert.assertEquals("k1", map.inverse().get("v2"));
    }
}
