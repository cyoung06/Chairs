package kr.syeyoung.chair;

import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.nbt.CommonTagCompound;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import kr.syeyoung.chair.data.Chair;
import kr.syeyoung.chair.data.ChairData;
import kr.syeyoung.chair.data.ChairEditSession;
import kr.syeyoung.chair.gui.ChairEditorGui;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ChairCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage("§b[의자] §c권한이 없습니다");
            return true;
        }

        if (command.getLabel().equalsIgnoreCase("의자")) {
            if (args.length == 0) {
                commandSender.sendMessage("§b[의자]§7 ================= 멋-진 의-자 플-러-그-인 =================");
                commandSender.sendMessage("§b[의자] §f/의자 - 해당 도움말을 확인합니다");
                commandSender.sendMessage("§b[의자] §f/의자 목록 - 로딩된 의자 목록을 확인합니다");
                commandSender.sendMessage("§b[의자] §f/의자 리로드 - 의자 파일들을 리로드 합니다");
                commandSender.sendMessage("§b[의자] §f/의자 지급 [의자ID] - 의자를 지급합니다");
                commandSender.sendMessage("§b[의자] §f/의자 에디터지급 [의자ID] - 의자 에디터를 지급합니다");
                commandSender.sendMessage("§b[의자] §f/의자 생성 [의자ID] - 새로운 의자를 생성하고 에디터를 지급합니다");
                commandSender.sendMessage("§b[의자] §f/의자 아이템설정 [의자ID] - 손에 든 아이템을 의자 아이템으로 설정합니다");
                commandSender.sendMessage("§b[의자] §f/의자 주변의자강제삭제 - 주변 10칸 내에 존재하는 의자를 강제삭제합니다");
            } else if (args[0].equals("리로드")) {
                Chairs.getInstance().loadChairs();
                commandSender.sendMessage("§b[의자] §f정상적으로 콘피그를 리로드 했다고 플러그인은 믿습니다");
            }  else if (args[0].equals("주변의자강제삭제")) {
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage("§b[의자] §c플레이어만 해당 명령어가 사용 가능합니다");
                    return true;
                }
                Location l =((Player) commandSender).getLocation();
                List<Chair> chairsToDelete = new ArrayList<>();
                Map<UUID, Chair> chairmap = Chairs.getInstance().getPlacedChairMap();
                for (Chair value : chairmap.values()) {
                    if (!value.getCenter().getWorld().equals(l.getWorld())) continue;
                    if (value.getCenter().distanceSquared(l) <= 100) {
                        chairsToDelete.add(value);
                    }
                }

                for (Chair c:chairsToDelete) {
                    c.deSummon(false, true);
                    chairmap.remove(c.getChairIdentifier());
                }
                Map<UUID, ChairEditSession> editSessionMap = Chairs.getInstance().getEditSessionMap();

                List<ChairEditSession> chairEditsToDelete = new ArrayList<>();
                for (ChairEditSession ces:editSessionMap.values()) {
                    if (!ces.getCenter().getWorld().equals(l.getWorld())) continue;
                    if (ces.getCenter().distanceSquared(l) <= 100) {
                        chairEditsToDelete.add(ces);
                    }
                }

                for (ChairEditSession chairEditSession : chairEditsToDelete) {
                    chairEditSession.remove(true);
                }

                commandSender.sendMessage("§b[의자] §f설치된 의자 "+chairsToDelete.size()+"개");
                commandSender.sendMessage("§b[의자] §f의자 편집 세션 "+chairEditsToDelete.size()+"개");
                commandSender.sendMessage("§b[의자] §f가 삭제되었습니다! 명령어를 다시 실행하여 정상적으로 삭제 되었음을 확인해주세요");
            }else if (args[0].equals("목록")) {
                commandSender.sendMessage("§b[의자]§7 ================= 멋-진 의-자 플-러-그-인 =================");
                Chairs.getInstance().getChairs().values().stream().forEach(cd -> {
                    TextComponent base = new TextComponent();
                    TextComponent hoverToCopy = new TextComponent();
                    TextComponent hoverToGet = new TextComponent();
                    {
                        hoverToCopy.setText("클릭하여 복사하기");
                        hoverToCopy.setColor(ChatColor.GREEN);
                    }
                    {
                        hoverToGet.setText("클릭하여 아이템 얻기");
                        hoverToGet.setColor(ChatColor.GREEN);
                    }
                    {
                        TextComponent extra1 = new TextComponent("이름: ");
                        extra1.setColor(ChatColor.WHITE);
                        base.addExtra(extra1);
                    }
                    {
                        TextComponent extra1 = new TextComponent(ChatColor.translateAlternateColorCodes('&', cd.getVisibleChairName()));
                        extra1.setColor(ChatColor.WHITE);
                        extra1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cd.getVisibleChairName().replace("§", "&")));
                        extra1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {hoverToCopy}));
                        base.addExtra(extra1);
                    }
                    {
                        TextComponent extra1 = new TextComponent(", 의자ID: ");
                        extra1.setColor(ChatColor.WHITE);
                        base.addExtra(extra1);
                    }
                    {
                        TextComponent extra1 = new TextComponent(cd.getChairIdentifier());
                        extra1.setColor(ChatColor.WHITE);
                        extra1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cd.getChairIdentifier()));
                        extra1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {hoverToCopy}));
                        base.addExtra(extra1);
                    }
                    {
                        TextComponent extra1 = new TextComponent(" [아이템얻기] ");
                        extra1.setColor(ChatColor.GREEN);
                        extra1.setBold(true);
                        extra1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/의자 지급 "+cd.getChairIdentifier()));
                        extra1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {hoverToGet}));
                        base.addExtra(extra1);
                    }
                    {
                        TextComponent extra1 = new TextComponent(" [에디터얻기] ");
                        extra1.setColor(ChatColor.AQUA);
                        extra1.setBold(true);
                        extra1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/의자 에디터지급 "+cd.getChairIdentifier()));
                        extra1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {hoverToGet}));
                        base.addExtra(extra1);
                    }
                    commandSender.spigot().sendMessage(base);
                });
            } else if (args[0].equals("지급")) {
                if (args.length != 2) {
                    commandSender.sendMessage("§b[의자] §c올바른 사용법: /의자 지급 [의자ID]");
                    return true;
                }
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage("§b[의자] §c플레이어만 해당 명령어가 사용 가능합니다");
                    return true;
                }

                String id = args[1];
                ChairData c = Chairs.getInstance().getChair(id);
                if (c == null) {
                    commandSender.sendMessage("§b[의자] §c존재하지 않는 의자입니다");
                    return true;
                }

                ((Player) commandSender).getInventory().addItem(c.getChairItem());
                commandSender.sendMessage("§b[의자] §f정상적으로 아이템을 지급하였습니다");
            }else if (args[0].equals("에디터지급")) {
                if (args.length != 2) {
                    commandSender.sendMessage("§b[의자] §c올바른 사용법: /의자 에디터지급 [의자ID]");
                    return true;
                }
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage("§b[의자] §c플레이어만 해당 명령어가 사용 가능합니다");
                    return true;
                }

                String id = args[1];
                ChairData c = Chairs.getInstance().getChair(id);
                if (c == null) {
                    commandSender.sendMessage("§b[의자] §c존재하지 않는 의자입니다");
                    return true;
                }

                ItemStack item = MapDisplay.createMapItem(Chairs.getInstance(), ChairEditorGui.class);
                ItemUtil.setDisplayName(item, "§b의자 에디터");
                CommonTagCompound ctc = ItemUtil.getMetaTag(item);
                ctc.putValue("ChairID", id);
                ((Player) commandSender).getInventory().addItem(item);
                commandSender.sendMessage("§b[의자] §f정상적으로 아이템을 지급하였습니다");
            }else if (args[0].equals("아이템설정")) {
                if (args.length != 2) {
                    commandSender.sendMessage("§b[의자] §c올바른 사용법: /의자 아이템설정 [의자ID]");
                    return true;
                }
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage("§b[의자] §c플레이어만 해당 명령어가 사용 가능합니다");
                    return true;
                }

                String id = args[1];
                ChairData c = Chairs.getInstance().getChair(id);
                if (c == null) {
                    commandSender.sendMessage("§b[의자] §c존재하지 않는 의자입니다");
                    return true;
                }

                ItemStack item = ((Player) commandSender).getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                if (!meta.hasLore()) lore = new ArrayList<>();
                lore.add("§의§자§" + String.join("§",id.split("")));
                meta.setLore(lore);
                item.setItemMeta(meta);
                c.setChairItem(item);

                YamlConfiguration yaml = new YamlConfiguration();
                yaml.set("chairData", c);
                try {
                    yaml.save(c.getFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }


                commandSender.sendMessage("§b[의자] §f정상적으로 아이템을 설정 및 수정하였습니다");
            }else if (args[0].equals("생성")) {
                if (args.length != 2) {
                    commandSender.sendMessage("§b[의자] §c올바른 사용법: /의자 생성 [의자ID]");
                    return true;
                }
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage("§b[의자] §c플레이어만 해당 명령어가 사용 가능합니다");
                    return true;
                }

                String id = args[1];
                ChairData c = Chairs.getInstance().getChair(id);
                if (c != null) {
                    commandSender.sendMessage("§b[의자] §c이미 존재하는 의자입니다");
                    return true;
                }

                ChairData cd = new ChairData();
                cd.setChairIdentifier(id);
                cd.setVisibleChairName(id);

                YamlConfiguration yaml = new YamlConfiguration();
                yaml.set("chairData", cd);
                try {
                    yaml.save(new File(Chairs.getInstance().getDataFolder(), id+".yml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cd.setFile(new File(Chairs.getInstance().getDataFolder(), id+".yml"));

                Chairs.getInstance().getChairs().put(id, cd);

                ItemStack item = MapDisplay.createMapItem(Chairs.getInstance(), ChairEditorGui.class);
                ItemUtil.setDisplayName(item, "§b의자 에디터");
                CommonTagCompound ctc = ItemUtil.getMetaTag(item);
                ctc.putValue("ChairID", id);
                ((Player) commandSender).getInventory().addItem(item);
                commandSender.sendMessage("§b[의자] §f정상적으로 아이템을 지급하였습니다");
            } else {
                commandSender.sendMessage("§b[의자] §c존재하지 않는 명령어입니다. /의자 를 입력하여 도움말을 확인해보세요");
            }
        }
        return true;
    }
}
