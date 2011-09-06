package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import static org.eclipse.swt.SWT.RADIO;
import j.combot.command.AltArg;
import j.combot.command.Arg;
import j.combot.gui.misc.GuiUtil;
import j.swt.util.SwtStdValues;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class AltVisual extends BaseArgVisual<Integer> {

	private Button radioBtn;

	private List<Button> titles = new ArrayList<>();
	private List<Composite> childComps = new ArrayList<>();

	@Override
	public Integer getValue() {
		return 0;
	}

	@Override
	public void makeWidget(
			Arg<Integer> part, Composite parent, VisualFactory visualFactory )
	{
		AltArg optArg = (AltArg) part;

		Label title = new Label( parent, NONE );
		title.setText( optArg.getTitle() );

		Composite radioParent = new Composite( parent, NONE );
		radioParent.setLayout( new GridLayout( 2, false ) );
		GridData radioData = new GridData( FILL, LEFT, true, false );
		radioData.horizontalSpan = 2;
		radioData.horizontalIndent = (int) ( SwtStdValues.UNIT * 1.5 );
		radioParent.setLayoutData( radioData );

		// Loop over children and add them reursivly.
		for ( Arg<?> arg : optArg.getArgGroup() ) {

			// Button that enables/disables children
			radioBtn = new Button( radioParent, RADIO );
			radioBtn.setText( optArg.getTitle() );
			GridData butData = new GridData();
			butData.horizontalSpan = 2;
			radioBtn.setLayoutData( butData );
			setValueControl( radioBtn );

			titles.add( radioBtn );

			// Add panel for children
			final Composite childsComp = new Composite( radioParent, NONE );
			childsComp.setLayout( new GridLayout( 2, false ) );
			GridData compData = new GridData( FILL, LEFT, true, false );
			compData.horizontalSpan = 2;
			compData.horizontalIndent = (int) ( SwtStdValues.UNIT * 1.5 );
			childsComp.setLayoutData( compData );

			childComps.add( childsComp );

			radioBtn.addSelectionListener( new SelectionAdapter() {
				@Override public void widgetSelected( SelectionEvent e ) {
					setEnabledFromSelected();
				}
			} );

			GuiUtil.createVisual( arg, childsComp, visualFactory );
		}

//		SwtUtil.recursiveSetEnabled( childsComp, isEnabled() );


	}

	private void setEnabledFromSelected() {
		// TODO: This enables/disables ALL children, should only be
		// parent. When switching back, children should regain their
		// previous state, not all of them should be enabled.
//					SwtUtil.recursiveSetEnabled( childsComp, isEnabled() );

	}
}
