package CET46InSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import CET46InSpire.events.CallOfCETRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NeowEventPatch {
    private static final Logger logger = LogManager.getLogger(NeowEventPatch.class.getName());
    @SpirePatch(clz = NeowEvent.class, method = "buttonEffect", paramtypez = {int.class})
    public static class InstantToCETEvent{
        @SpireInsertPatch(rloc = 82)
        public static SpireReturn<Void> Insert() {
            logger.info("Floor num: {}", AbstractDungeon.floorNum);
            if (AbstractDungeon.floorNum == 0) {
                (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMPLETE;
                logger.info("To call of CET room");
                RoomEventDialog.optionList.clear();
                MapRoomNode node = new MapRoomNode(0, -1);
                node.room = new CallOfCETRoom();
                AbstractDungeon.player.releaseCard();
                AbstractDungeon.overlayMenu.hideCombatPanels();
                AbstractDungeon.previousScreen = null;
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.dungeonMapScreen.closeInstantly();
                AbstractDungeon.closeCurrentScreen();
                AbstractDungeon.topPanel.unhoverHitboxes();
                AbstractDungeon.fadeIn();
                AbstractDungeon.effectList.clear();
                AbstractDungeon.topLevelEffects.clear();
                AbstractDungeon.topLevelEffectsQueue.clear();
                AbstractDungeon.effectsQueue.clear();
                AbstractDungeon.dungeonMapScreen.dismissable = true;
                AbstractDungeon.nextRoom = node;
                AbstractDungeon.setCurrMapNode(node);
                AbstractDungeon.getCurrRoom().onPlayerEntry();
                AbstractDungeon.rs = (node.room.event instanceof com.megacrit.cardcrawl.events.AbstractImageEvent) ? AbstractDungeon.RenderScene.EVENT : AbstractDungeon.RenderScene.NORMAL;
                AbstractDungeon.nextRoom = null;

                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "populatePathTaken", paramtypez = {SaveFile.class})
    public static class LocateSaveFile {
        @SpirePrefixPatch
        public static void Prefix(AbstractDungeon __instance, SaveFile saveFile) {
            if (saveFile.current_room.equals(CallOfCETRoom.class.getName())) {
                logger.info("To Neow room");
                saveFile.current_room = NeowRoom.class.getName();
            }
        }

    }
}
