package com.shaurmalay.bot.services;

import com.shaurmalay.bot.dao.GoodDao;
import com.shaurmalay.bot.dao.GoodInCartDao;
import com.shaurmalay.bot.model.Buff;
import com.shaurmalay.bot.model.Cart;
import com.shaurmalay.bot.model.Good;
import com.shaurmalay.bot.model.GoodInCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GoodInCartService {
    private GoodInCartDao goodInCartDao;
    private GoodDao goodDao;

    @Autowired
    public GoodInCartService(GoodInCartDao goodInCartDao, GoodDao goodDao) {
        this.goodInCartDao = goodInCartDao;
        this.goodDao = goodDao;
    }

    public List<Good> getGoodsInCart(Cart cart) {
        Optional<List<GoodInCart>> goodsIncart = goodInCartDao.findAllByCart(cart);
        List<Good> goods = null;
        if (goodsIncart.isPresent()) {
             goods = goodsIncart.get().stream().map(gic -> gic.getGoodCallbacck())
                    .map(gc -> goodDao.getGoodByCallBack(gc.toUpperCase())).collect(Collectors.toList());
        }
        return goods;
    }
    public String getGoodInCartTostring(Cart cart, Long goodInCartId) {
        StringBuilder sb = new StringBuilder();
        GoodInCart goodInCart = goodInCartDao.findById(goodInCartId).get();
        sb.append(goodDao.getGoodByCallBack(goodInCart.getGoodCallbacck()) + " ");
        if (goodInCart.getBuffs() != null && !goodInCart.getBuffs().isEmpty()) {
            sb.append("(");
            sb.append(goodInCart.getBuffs().toString()
                    .replace("[", "")
                    .replace("]", ""));
            sb.append(")");
        }
        return sb.toString();
    }
    public int calculateSumForGoodsByCart(Cart cart) {
        List<Good> goods = getGoodsInCart(cart);
        List<GoodInCart> goodsInCart = goodInCartDao.findAllByCart(cart).get();
        if (goods == null && goodsInCart == null) {
            return 0;
        }
        int goodSum = goods.stream().mapToInt(g -> g.getPrice()).sum();
        int buffsSum = goodsInCart.stream()
                .filter(good -> good.getBuffs() != null && !good.getBuffs().isEmpty())
                .flatMap(g -> g.getBuffs().stream())
                .mapToInt(Buff::getPrice).sum();
        return goodSum + buffsSum;
    }
    public String getGoodsWithBuffsToString(Cart cart) {
        StringBuilder sb = new StringBuilder();

        List<GoodInCart> gic= goodInCartDao.findAllByCart(cart).get();
        if (gic.isEmpty() || gic == null) {
            sb.append("В корзине пока пусто");
            return sb.toString();
        }

        for (GoodInCart goodInCart : gic) {
            sb.append(goodDao.getGoodByCallBack(goodInCart.getGoodCallbacck()));
            if (goodInCart.getBuffs() != null && !goodInCart.getBuffs().isEmpty()) {
                sb.append("(");
                sb.append(goodInCart.getBuffs().toString()
                        .replace("[", "")
                        .replace("]", ""));
                sb.append(")");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void deleteAllFromCart(Cart cart) {
        goodInCartDao.deleteAllByCart_Id(cart.getId());
    }
    public void deleteGoodFromCartById(Long id) {
        goodInCartDao.deleteById(id);
    }
}
