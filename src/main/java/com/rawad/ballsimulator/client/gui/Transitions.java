package com.rawad.ballsimulator.client.gui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public final class Transitions {
	
	public static final Transition EMPTY = emptyParallel(null, null);
	
	public static final Duration DEFAULT_DURATION = Duration.millis(500);
	
	/** Alpha value indicating an opaque object. */
	public static final double OPAQUE = 1.0d;
	/** Alpha value indicating an object that is mostly hidden. */
	public static final double HIDDEN = 0.1d;
	
	private Transitions() {}
	
	public static final ParallelTransition stateOnActivate(Region target, Duration duration) {
		ParallelTransition transition = emptyParallel(target, null);
		
		Transition slide = slideHorizontally(target, duration, target.getWidth(), 0);
		Transition fade = fade(target, duration, OPAQUE);
		
		transition.getChildren().addAll(slide, fade);
		
		return transition;
	}
	
	public static final ParallelTransition stateOnDeactivate(Region target, Duration duration) {
		ParallelTransition transition = emptyParallel(target, null);
		
		Transition slide = slideHorizontally(target, duration, 0, target.getWidth());
		Transition fade = fade(target, duration, HIDDEN);
		
		transition.getChildren().addAll(slide, fade);
		
		return transition;
	}
	
	public static final Transition exit(Node target, Duration duration, EventHandler<ActionEvent> onFinish) {
		ParallelTransition transition = emptyParallel(target, onFinish);
		
		FadeTransition fade = fade(target, duration, HIDDEN);
		
		transition.getChildren().addAll(fade);
		
		return transition;
	}
	
	public static final TranslateTransition slideHorizontally(Node node, Duration duration, double from, double to) {
		TranslateTransition slide = new TranslateTransition(duration, node);
		slide.setFromX(from);
		slide.setToX(to);
		return slide;
	}
	
	public static final FadeTransition fade(Node node, Duration duration, double fadeTo) {
		FadeTransition fade = new FadeTransition(duration, node);
		fade.setFromValue(node.getOpacity());
		fade.setToValue(fadeTo);
		return fade;
	}
	
	public static final ParallelTransition emptyParallel(Node target, EventHandler<ActionEvent> onFinish) {
		ParallelTransition transition = new ParallelTransition(target);
		transition.setOnFinished(onFinish);
		return transition;
	}
	
}
