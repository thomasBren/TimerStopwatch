package states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import states.stopwatch.AbstractStopwatch;
import states.stopwatch.LaptimeStopwatch;
import states.stopwatch.ResetStopwatch;
import states.stopwatch.RunningStopwatch;
import states.timer.AbstractTimer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class StopwatchTests {

	private static Context context;
	private ClockState current;

	@BeforeEach
	public void setup() {
        context = new Context(); // create the state machine context
        AbstractStopwatch.resetInitialValues();
        context.currentState = AbstractStopwatch.Instance();
	}
		
	@org.junit.jupiter.api.Test
	public void testInitialState() {
		//context.tick(); //no tick() needed for this test;
		/* When initialising the context (see setup() method above)
		 * its currentState will be initialised with the initial state
		 * of timer, i.e. the IdleTimer state.
		 */
		current = context.currentState;
		
	    assertEquals(Mode.stopwatch, current.getMode());
	    assertSame(ResetStopwatch.Instance(), current);
	    assertEquals(0, AbstractStopwatch.getTotalTime(),"For the value of totalTime we ");
	    assertEquals(0, AbstractStopwatch.getLapTime(),"For the value of lapTime we ");
	}

	@Test
	public void testInitialAbstractStopwatch() {
		// The initial state of composite state AbstractStopwatch should be ResetStopwatch
		assertSame(AbstractStopwatch.Instance(), ResetStopwatch.Instance());
	}

	@org.junit.jupiter.api.Test
	public void testUp(){
		current = LaptimeStopwatch.Instance();
		ClockState newState = current.up();
		assertEquals(RunningStopwatch.Instance(), newState);
	}

	@org.junit.jupiter.api.Test
	public void testDoIt(){
		current = LaptimeStopwatch.Instance();
		current.entry();
		current=current.doIt();
		assertEquals(LaptimeStopwatch.Instance(), current);

		current=current.doIt();
		current=current.doIt();
		current=current.doIt();
		assertEquals(RunningStopwatch.Instance(), current=current.doIt());
	}
	
	@org.junit.jupiter.api.Test
	public void testHistoryState() {		
		current = AbstractStopwatch.Instance();
		// after processing the left() event, we should arrive in the initial state of AbstractStopwatch
		ClockState newState = current.left();
		assertEquals(AbstractTimer.Instance(), newState);
		/* after another occurrence of the left() event, we should return to the original state
		 * because we used history states		
		 */
		assertEquals(current, newState.left());
	}

}
