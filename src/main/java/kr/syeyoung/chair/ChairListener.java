package kr.syeyoung.chair;

import kr.syeyoung.chair.data.Chair;
import kr.syeyoung.chair.data.ChairData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;

public class ChairListener implements Listener {

    @EventHandler
    public void commandPreProcess(PlayerCommandPreprocessEvent event) {
        Player p =event.getPlayer();
        if (p.getVehicle() != null && p.getVehicle().hasMetadata("ChairID")) {
            p.sendMessage("§b[의자] §f명령어는 의자에서 내리고 사용해주세요");
            event.setCancelled(true);
            return;
        }
    }


    @EventHandler
    public void onInteractToSpawn(PlayerInteractEvent e) {
        if (!(e.hasBlock() && e.hasItem())) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = e.getItem();
        if (!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return;
        List<String> lore = meta.getLore();
        String lastLineLore = lore.get(lore.size() - 1);
        if (!lastLineLore.startsWith("§의§자")) return;
        String identifier = lastLineLore.substring(4).replace("§", "");
        if (identifier.isEmpty()) return;
        ChairData cd = Chairs.getInstance().getChair(identifier);
        if (cd == null) return;
        e.setCancelled(true);

        if (cd.getEntityList().size() == 0) {
            e.getPlayer().sendMessage("§b[의자] §f해당 의자에는 엔티티가 하나도 없어 설치할 수 없습니다");
            return;
        }

        if (e.getPlayer().getVehicle() != null && e.getPlayer().getVehicle().hasMetadata("ChairID")) {
            e.getPlayer().sendMessage("§b[의자] §f의자에 앉아있는 도중에는 의자를 설치할 수 없습니다.");
            return;
        }

        if (Chairs.getInstance().getBlacklistedWorlds().contains(e.getPlayer().getWorld().getName())) {
            e.getPlayer().sendMessage("§b[의자] §f의자 설치가 금지된 월드입니다");
            return;
        }


        Block toPlaced = e.getClickedBlock().getRelative(e.getBlockFace());

        if (!toPlaced.isEmpty()) {
            return;
        }

        if (toPlaced.getRelative(BlockFace.DOWN).isEmpty()) {
            return;
        }


        item.setAmount(item.getAmount() - 1);


        Location l = toPlaced.getLocation();
        l.add(0.5, 0, 0.5);
        Vector direction = e.getPlayer().getLocation().getDirection().multiply(-1);
        direction.setY(0);
        l.setDirection(direction);

        Chair c = new Chair(cd, l, e.getPlayer());
        c.summon();

        ride(e.getPlayer(), c);
    }

    public void check(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() == null) return;
        Entity e = event.getRightClicked();
        if (!e.hasMetadata("ChairID")) return;
        UUID cID = UUID.fromString(e.getMetadata("ChairID").get(0).asString());
        Chair c = Chairs.getInstance().getPlacedChair(cID);
        if (c == null) return;
        event.setCancelled(true);
        if (event.getPlayer().getVehicle() != null) return;

        ride(event.getPlayer(), c);
    }

    public void ride(Player p, Chair c) {
        for (Entity en : c.getEntities()) {
            if (en.getMetadata("ChairSittable").get(0).asBoolean()) {
                if (en.getPassengers().size() != 0) continue;
                en.addPassenger(p);
                break;
            }
        }
    }

    public void leave(Player p) {
        if (p.getVehicle() == null) return;
        Entity vehicle = p.getVehicle();
        if (!vehicle.hasMetadata("ChairID")) return;
        UUID cID = UUID.fromString(vehicle.getMetadata("ChairID").get(0).asString());
        Chair c = Chairs.getInstance().getPlacedChair(cID);
        if (c == null) return;
        vehicle.eject();
        if (p.getUniqueId().equals(c.getChairOwnerUUID())) {
            despawn(c);
        }
    }

    @EventHandler
    public void onCollision(VehicleEntityCollisionEvent evt) {
        if (evt.getVehicle() instanceof LivingEntity) {
            if (!((LivingEntity) evt.getVehicle()).isCollidable()) {
                evt.setCollisionCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().spigot().setCollidesWithEntities(false);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (event.getPlayer().getVehicle() != null && event.getPlayer().getVehicle().hasMetadata("ChairID")) {
            Entity e = event.getPlayer().getVehicle();
            if (!e.hasMetadata("ChairID")) return;
            UUID cID = UUID.fromString(e.getMetadata("ChairID").get(0).asString());
            Chair c = Chairs.getInstance().getPlacedChair(cID);
            if (c == null) return;
            if (!c.getChairOwnerUUID().equals(event.getPlayer().getUniqueId())) {
                return;
            }
            despawn(c);
        }
    }

    public void despawn(Chair c) {
        c.deSummon(true, false);
        ItemStack chairItem = c.getChairData().getChairItem();
        Bukkit.getPlayer(c.getChairOwnerUUID()).getInventory().addItem(chairItem);
    }

    @EventHandler
    public void damageCancelForEditor(EntityDamageByEntityEvent event) {
        if (event.getEntity() != null && event.getEntity().hasMetadata("Editor")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void fireCancelForEveryChair(EntityCombustEvent event) {
        if (event.getEntity() != null && (event.getEntity().hasMetadata("Editor") || event.getEntity().hasMetadata("ChairID"))) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void chairDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() != null && (event.getDamager().hasMetadata("Editor") || event.getDamager().hasMetadata("ChairID"))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = false)
    public void onInteractToDespawn(EntityDamageByEntityEvent event) {

        Entity e = event.getEntity();
        if (e == null) return;

        if (!e.hasMetadata("ChairID")) return;
        UUID cID = UUID.fromString(e.getMetadata("ChairID").get(0).asString());
        Chair c = Chairs.getInstance().getPlacedChair(cID);
        if (c == null) return;
        event.setCancelled(true);
        if (!(event.getDamager() instanceof Player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (event.getDamager().getVehicle() != null) {
            return;
        }
        if (!c.getChairOwnerUUID().equals(event.getDamager().getUniqueId())) {
            event.getDamager().sendMessage("§b[의자] §f자신의 의자만 부술 수 있습니다");
            return;
        }
        despawn(c);
    }

    @EventHandler
    public void onInteractToRide(PlayerInteractEntityEvent event) {
        check(event);
    }

    @EventHandler
    public void onInteractToRide2(PlayerInteractAtEntityEvent event) {
        check(event);
    }

    public void onPlayerSteer(Player p) {
        leave(p);
    }


    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (event.isCancelled()) return;
        if (!event.isSneaking()) return;
        List<Entity> entities = event.getPlayer().getNearbyEntities(0.3,1,0.3);
        for (Entity e: entities) {
            if (!e.hasMetadata("ChairID")) continue;
            UUID cID = UUID.fromString(e.getMetadata("ChairID").get(0).asString());
            Chair c = Chairs.getInstance().getPlacedChair(cID);
            if (c == null) continue;
            if (e.getMetadata("ChairSittable").get(0).asBoolean()) {
                if (e.getPassengers().size() != 0) continue;
                event.setCancelled(true);
                e.addPassenger(event.getPlayer());
                return;
            }
        }
    }
}
