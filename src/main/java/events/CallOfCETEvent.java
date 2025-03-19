package events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import helpers.DayBeforeCETPlayGameException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relics.BookOfCET4;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

public class CallOfCETEvent extends AbstractImageEvent {
    private static final Logger logger = LogManager.getLogger(CallOfCETEvent.class.getName());
    public static final String ID = "CET46:CallOfCETEvent";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static String INTRO_BODY;
    private static String ACCEPT_BODY;
    private static String REFUSE_BODY;
    private static final String OPTION_REFUSE;
    private static final String OPTION_EXIT;
    private static final String OPTION_STUDY;
    private static final LocalDateTime NEXT_CET;
    private static final LocalDateTime NEXT_NEXT_CET;
    private ArrayList<BookEnum> validBooks;
    private EventState state;
    private boolean tomorrowCET = false;

    public CallOfCETEvent() {
        super(eventStrings.NAME, "test", "image/events/call_of_cet.png");
        this.tomorrowCET = false;
        this.initBodies();
        this.body = INTRO_BODY;
        state = EventState.INTRO;

        this.initBook();
        if (this.tomorrowCET) {
            this.imageEventText.setDialogOption(OPTION_STUDY);
        }
        this.imageEventText.setDialogOption(OPTION_REFUSE);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (state) {
            case INTRO: {
                if (buttonPressed == this.imageEventText.optionList.size() - 1) {
                    // refuse
                    this.state = EventState.REFUSE;
                    this.imageEventText.updateBodyText(REFUSE_BODY);
                    this.imageEventText.updateDialogOption(0, OPTION_EXIT);
                    if (this.tomorrowCET) {
                        // now total option: 3, so need to set 1
                        this.imageEventText.updateDialogOption(1, OPTION_STUDY);
                        this.imageEventText.updateDialogOption(2, OPTION_STUDY);
                        this.imageEventText.setDialogOption(OPTION_STUDY);
                        this.clearOptions(4);
                    } else {
                        this.clearOptions();
                    }
                    return;
                } else if (this.tomorrowCET && buttonPressed == this.imageEventText.optionList.size() - 2) {
                    // study
                    gotoStudyNow();
                    return;
                }
                // accept
                this.state = EventState.ACCEPT;
                AbstractRelic relicMetric = getBookRelic(this.validBooks.get(buttonPressed));
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(0.5F * Settings.WIDTH, 0.5F * Settings.HEIGHT, relicMetric);
                this.imageEventText.updateBodyText(ACCEPT_BODY);
                this.imageEventText.updateDialogOption(0, OPTION_EXIT);
                if (this.tomorrowCET) {
                    this.imageEventText.updateDialogOption(1, OPTION_STUDY);
                    this.clearOptions(2);
                } else {
                    this.clearOptions();
                }
                return;
            }
            case ACCEPT:
            case REFUSE:
                if ((!this.tomorrowCET) || buttonPressed == 0) {
                    openMap();
                    return;
                }
                // study
                gotoStudyNow();
                return;

        }
        openMap();
    }

    private void initBodies() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime next = NEXT_CET;
        String duration_data = eventStrings.DESCRIPTIONS[2];
        if (today.isAfter(NEXT_CET)) {
            next = NEXT_NEXT_CET;
        }
        if (next != null) {
            Duration duration = Duration.between(today, next);
            long duration_ = duration.toDays();
            if (duration_ < 1) {
                duration_ = 1;
            }
            duration_data = String.valueOf(duration_);
            if (duration_ == 1) {
                this.tomorrowCET = true;
            }
        }
        if (this.tomorrowCET) {
            duration_data = eventStrings.DESCRIPTIONS[6];
        } else {
            duration_data = duration_data + eventStrings.DESCRIPTIONS[5];
        }
        INTRO_BODY = eventStrings.DESCRIPTIONS[0] + duration_data + eventStrings.DESCRIPTIONS[1];
        REFUSE_BODY = eventStrings.DESCRIPTIONS[3];
        ACCEPT_BODY = eventStrings.DESCRIPTIONS[4];
        if (this.tomorrowCET) {
            INTRO_BODY += eventStrings.DESCRIPTIONS[7];
            REFUSE_BODY += eventStrings.DESCRIPTIONS[8];
            ACCEPT_BODY += eventStrings.DESCRIPTIONS[9];
        }
    }

    private void initBook() {
        this.validBooks = new ArrayList<>();
        for (BookEnum b: BookEnum.values()) {
            this.imageEventText.setDialogOption(getBookOption(b));
            this.validBooks.add(b);
        }
    }

    public static String getBookOption(BookEnum b) {
        switch (b) {
            case CET4:
                return eventStrings.OPTIONS[4];
        }
        logger.info("wtf? what happened?");
        return eventStrings.OPTIONS[3];
    }

    public static AbstractRelic getBookRelic(BookEnum b) {
        switch (b) {
            case CET4:
                return RelicLibrary.getRelic(BookOfCET4.ID).makeCopy();
        }
        logger.info("wtf? what happened?");
        return RelicLibrary.getRelic("Circlet").makeCopy();
    }

    private void gotoStudyNow() {
        throw new DayBeforeCETPlayGameException();
    }

    public void clearOptions(int remainNum) {
        for(int i = this.imageEventText.optionList.size() - 1; i > remainNum - 1; --i) {
            this.imageEventText.optionList.remove(i);
        }

        Iterator var3 = this.imageEventText.optionList.iterator();

        while(var3.hasNext()) {
            LargeDialogOptionButton b = (LargeDialogOptionButton)var3.next();
            b.calculateY(this.imageEventText.optionList.size());
        }

    }

    public void clearOptions() {
        this.clearOptions(1);
    }

    static {
        OPTION_EXIT = eventStrings.OPTIONS[0];
        OPTION_REFUSE = eventStrings.OPTIONS[1];
        OPTION_STUDY = eventStrings.OPTIONS[2];

        NEXT_CET = LocalDateTime.of(2025, 6, 14, 0, 0, 0);
        NEXT_NEXT_CET = null;
    }

    public enum EventState {
        INTRO,
        ACCEPT,
        REFUSE,
    }

    public enum BookEnum {
        CET4
    }

}